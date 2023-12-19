Function UpdateHashTable($hashTable, $oldName, $newName) 
{	
	try{	
		if ($hashTable.ContainsKey($oldName) -eq $false){
            $normalizedName = $newName.toString() -replace '#','_' -replace '"','_' -replace '/','_'-replace '\[','_'  -replace ']','_'  -replace '%','_' -replace '\*','_' -replace ':','_' -replace '<','_' -replace '>','_' -replace '\?','_'  -replace '\|','_'

             if ($normalizedName.Substring(0,1) -eq " " -or $normalizedName.Substring(0,1) -eq "_"){
                $normalizedName = $normalizedName.Substring(1)
             }

            $normalizedName = $normalizedName.Trim()

            
			$hashTable.Add($oldName.ToString(),$normalizedName)
		}
	}catch{
		Write-LogError "Failed to update the hashtable, the key is null : $( $_.Exception)"
	}

    return $hashTable
}

function Get-ObjectMember {
    [CmdletBinding()]
    Param(
        [Parameter(Mandatory=$True, ValueFromPipeline=$True)]
        [PSCustomObject]$obj
    )
    $obj | Get-Member -MemberType NoteProperty | ForEach-Object {
        $key = $_.Name
        $val = $obj."$key"

        [PSCustomObject]@{Key = $key; Value = $val }
    }
}

Function GetMetadata($file, $metadata, $flux){
	$nodeContent = Get-Content -Raw $file  -Encoding UTF8 | ConvertFrom-Json
	$catToIgnore = @()
	if ($nodeContent."TRS Metadata"."CHORUS_Processus" -ne $null -and $nodeContent."TRS Metadata"."CHORUS_Nom" -ne $null) {
		$catToIgnore = @("PRS Metadata","GovOrga Metadata")
	}
	if ($nodeContent."PRS Metadata"."CHORUS_Processus" -ne $null -and $nodeContent."PRS Metadata"."CHORUS_Nom" -ne $null) {
		$catToIgnore = @("TRS Metadata","GovOrga Metadata")
	}
	if ($nodeContent."GovOrga Metadata"."CHORUS_Processus" -ne $null -and $nodeContent."GovOrga Metadata"."CHORUS_Nom" -ne $null) {
		$catToIgnore = @("TRS Metadata","PRS Metadata")
	}
	
	$fieldsToBeIgnored =@("TTOL_Workspace_Level","TTOL_ECM_Reference_Entity","CHORUS_Résumé")
	
	$nodeContent | Get-ObjectMember | ForEach-Object {
		Write-LogDebug -Content "Control Metadata Key: $($_.Key)"
       if ($catToIgnore -contains $_.Key) {
		   Write-LogDebug -Content "Control Metadata Key: $($_.Key) is rejected because found in $($catToIgnore)"
	   } else {
	   
		Write-LogDebug -Content "Control Metadata Key: $($_.Key) should be added"
       if ( $_.Value -and $_.Value.GetType().toString() -eq "System.Management.Automation.PSCustomObject"){
			$_.Value | Get-ObjectMember | ForEach-Object {
                $value = $_.Value -join ', '
                
				# Change date format from "25-04-2023" to "2023-04-25 00:00:00"
				if (@("CHORUS_Date") -contains $_.Key -and $value -ne "") {
					$date =[datetime]::parseexact($value,'dd-MM-yyyy', $null)
					$value = $date.ToString("yyyy-MM-dd hh:mm:ss")
				}
				
				if ($fieldsToBeIgnored -contains $_.Key) {
					Write-LogDebug -Content "Ignoring field $($_.Key)"
				} else {
					Write-LogDebug -Content "Field $($_.Key -replace 'é','e') is added in first case"
					if ($flux.$($_.Key) -ne $null -and $flux.$($_.Key) -ne "") {
						$metadata.Add("$($flux.$($_.Key) )", "$($value)" )
					}else{
						$metadata.Add("$($_.Key -replace 'é','e')", "$($value)" )
					}
				}
			}
		}ElseIf ($_.Value -and $_.Value.GetType().toString() -eq "System.Object[]"){
            Write-LogDebug -Content "Field $($_.Key) is added through ElseIf 1"
			if ( $flux.$($_.Key) -ne $null -and $flux.$($_.Key) -ne "") {
				$metadata.Add("$flux.$($_.Key) )", $($_.Value) )
			}else{
				$metadata.Add("$($_.Key -replace 'é','e')", $($_.Value) )
			}
		}else{
			Write-LogDebug -Content "Field $($_.Key) is added through ElseIf 2"
			if ( $flux.$($_.Key) -ne $null -and $flux.$($_.Key) -ne "") {
				$metadata.Add("$($flux.$($_.Key) )", "$($_.Value)" )
			}else{
				$metadata.Add("$($_.Key -replace 'é','e')", "$($_.Value)" )
			}
		} 
	   }
       
	}
	return $metadata
}

Function UpdateVersionMetadata($Context, $file, $metadata, $connection, $flux, $ContentType, $AllFields){
	Write-LogDebug -Content "Control UpdateVersionMetadata function"
	Write-LogDebug -Content "metadata $($metadata)"
	Write-LogDebug -Content "connection $($connection)"
	Write-LogDebug -Content "flux $($flux)"
	
	try{
        #$ContentType = Get-PnPContentType -Identity "TTOL Files"
#		-Connection $connection
    }catch{
		Write-LogError "Failed to update the metadata, the content type cannot be retrieved : $( $_.Exception)"
	}

    try{	
        $metadata = ValidateFileMetadata -metadata $metadata -flux $flux -AllFields $AllFields
# -Connection $connection 
		$Context.Load($file.ListItemAllFields)
		$Context.ExecuteQuery()
		
		#AEROW ORD parameter -UpdateType UpdateOverwriteVersion is unkown
        Set-PnPListItem -List "Documents" -Connection $connection -ContentType $ContentType -Identity $file.ListItemAllFields.Id -Values $metadata
	}catch{
		Write-LogError "Failed to update the metadata, it cannot be added to the target folder : $( $_.Exception)"
    }
    
	return $Context, $file
}

Function UpdateSiteMetadata_old($listName, $siteMetadata, $connection){

	try{	
		$listCard = Get-PnPList $listName -Connection $connection
	}catch{
		Write-LogError "Failed to update the site's metadata, the Identity List cannot be retrieved : $( $_.Exception)"
	}

	if ($listCard.ItemCount -eq 0){
		
		foreach ($Key in $siteMetadata.Keys) {
			
			try{	
				Add-PnPListItem -Connection $connection -List $listName -Values @{"Title"="$($Key)";"Value"="$($siteMetadata[$Key])"}
			}catch{
				Write-LogError "Failed to update the site's metadata, it cannot be added to the site : $( $_.Exception)"
			}
		}
	}
}

Function UpdateSiteMetadata($listName, $siteMetadata, $connection){

	Write-LogDebug -Content "UpdateSiteMetadata - listName $($listName)"
	Write-LogDebug -Content "UpdateSiteMetadata - siteMetadata $($siteMetadata)"
	try{	
		$listCard = Get-PnPList $listName -Connection $connection
	}catch{
		Write-LogError "Failed to update the site's metadata, the Identity List cannot be retrieved : $( $_.Exception)"
	}

	if ($listCard.ItemCount -eq 0){
		
		foreach ($Key in $siteMetadata.Keys) {
			if ($siteMetadata["$($Key)"].Equals('') -eq $false -and $siteMetadata["$($Key)"].Equals('?') -eq $false){
                try{	
				    Add-PnPListItem -Connection $connection -List $listName -Values @{"Title"="$($Key)";"Value"="$($siteMetadata[$Key])"}
			    }catch{
				    Write-LogError "Failed to update the site's metadata, it cannot be added to the site : $( $_.Exception)"
			    }
            }
			
		}
	}
}

Function ValidateFileMetadata($metadata, $connection, $flux, $AllFields){
	$comment = ""
	$validatedMetadata = @{}
	$CTfieldsToBeIgnored =@("$($flux)", "_Comments", "Created_x0020_By", "ContentType", "SelectFilename", "Modified_x0020_By","TTOL_Workspace_Level","TTOL_ECM_Reference_Entity","CHORUS_Résumé")
	
	Write-LogDebug -Content "Control ValidateFileMetadata function"
	Write-LogDebug -Content "metadata $($metadata)"
	Write-LogDebug -Content "connection $($connection)"
	Write-LogDebug -Content "flux $($flux)"
	
	try{	
		#$fields = Get-PnPProperty -ClientObject $contentType -property "Fields" -Connection $connection
		$fields = $AllFields | Where-Object {($CTfieldsToBeIgnored -contains $_.InternalName) -eq $false}
	}catch{
		Write-LogError "Failed to validate the metadata, the content type properties cannot be retrieved : $( $_.Exception)"
	}

	$fields | ForEach-Object{
		Write-LogDebug -Content "Control expected fields: $($_.InternalName)"
		if ($metadata.ContainsKey("$($_.InternalName)") -eq $true -and $($_.InternalName).Equals("$($flux)") -eq $false -and $metadata["$($_.InternalName)"].Equals('') -eq $false -and $metadata["$($_.InternalName)"].Equals('?') -eq $false){
			$validatedMetadata.Add("$($_.InternalName)", $metadata["$($_.InternalName)"])
		}
	}
	$MTfieldsToBeIgnored =@("$($flux)", "advancedversion", "subtype", "mimetype", "vermajor", "verminor", "vernum","TTOL_Workspace_Level","TTOL_ECM_Reference_Entity","CHORUS_Résumé")
	
	foreach ($Key in $metadata.Keys) {
		if($MTfieldsToBeIgnored -contains "$($Key)" -eq $false -and $validatedMetadata.ContainsKey("$($Key)") -eq $false -and $metadata["$($Key)"].Equals('') -eq $false -and $metadata["$($Key)"].Equals('?') -eq $false ){
			$comment = $comment + "$($Key)" + "; "
		}
	}

	if ($comment){
		#$validatedMetadata.add("_Comments", $comment)
	}

	return $validatedMetadata
}