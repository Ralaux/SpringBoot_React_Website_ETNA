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
	$nodeContent | Get-ObjectMember | ForEach-Object {
       
       if ( $_.Value -and $_.Value.GetType().toString() -eq "System.Management.Automation.PSCustomObject"){
			$_.Value | Get-ObjectMember | ForEach-Object {
                $value = $_.Value -join ', '
                
				if ($flux.$($_.Key) -ne $null -and $flux.$($_.Key) -ne "") {
					$metadata.Add("$($flux.$($_.Key) )", "$($value)" )
				}else{
					$metadata.Add("$($_.Key)", "$($value)" )
				}
			}
		}ElseIf ($_.Value -and $_.Value.GetType().toString() -eq "System.Object[]"){
            
			if ( $flux.$($_.Key) -ne $null -and $flux.$($_.Key) -ne "") {
				$metadata.Add("$flux.$($_.Key) )", $($_.Value) )
			}else{
				$metadata.Add("$($_.Key)", $($_.Value) )
			}
		}else{
			if ( $flux.$($_.Key) -ne $null -and $flux.$($_.Key) -ne "") {
				$metadata.Add("$($flux.$($_.Key) )", "$($_.Value)" )
			}else{
				$metadata.Add("$($_.Key)", "$($_.Value)" )
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
	$CTfieldsToBeIgnored =@("$($flux)", "_Comments", "Created_x0020_By", "ContentType", "SelectFilename", "Modified_x0020_By")
	
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
		if ($metadata.ContainsKey("$($_.InternalName)") -eq $true -and $($_.InternalName).Equals("$($flux)") -eq $false -and $metadata["$($_.InternalName)"].Equals('') -eq $false -and $metadata["$($_.InternalName)"].Equals('?') -eq $false){
			$validatedMetadata.Add("$($_.InternalName)", $metadata["$($_.InternalName)"])
		}
	}
	$MTfieldsToBeIgnored =@("$($flux)", "advancedversion", "subtype", "mimetype", "vermajor", "verminor", "vernum")
	
	foreach ($Key in $metadata.Keys) {
		if($MTfieldsToBeIgnored -contains "$($Key)" -eq $false -and $validatedMetadata.ContainsKey("$($Key)") -eq $false -and $metadata["$($Key)"].Equals('') -eq $false -and $metadata["$($Key)"].Equals('?') -eq $false ){
			$comment = $comment + "$($Key)" + "; "
		}
	}

	if ($comment){
		$validatedMetadata.add("_Comments", $comment)
	}

	return $validatedMetadata
}