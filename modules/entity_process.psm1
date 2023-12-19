Function PreprocessFolder($Context, $TargetFolder, $SourceFolderPath, $item, $hashTable, $connection){
    
    $FolderRelativeURL = $TargetFolder.ServerRelativeURL+$item.FullName.Replace($SourceFolderPath,[string]::Empty).Replace("\","/")
    $RelatifPathsNodes = $item.FullName.Replace($SourceFolderPath,[string]::Empty).Replace("\","/").split("/")
    $vide , $RelatifPathsNodesArray= $RelatifPathsNodes		

    Write-LogDebug -Content "FolderRelativeURL $($FolderRelativeURL)"
    Write-LogDebug -Content "item $($item)"
    Write-LogDebug -Content "item.FullName $($item.FullName)"
    Write-LogDebug -Content "SourceFolderPath $($SourceFolderPath)"
    Write-LogDebug -Content "item.FullName x1 replace ($SourceFolderPath,[string]::Empty) $($item.FullName.Replace($SourceFolderPath,[string]::Empty))"
    Write-LogDebug -Content "item.FullName x2 replace $($item.FullName.Replace($SourceFolderPath,[string]::Empty).Replace("\","/"))"
    Write-LogDebug -Content "RelatifPathsNodes $($RelatifPathsNodes)"
    Write-LogDebug -Content "RelatifPathsNodesArray $($RelatifPathsNodesArray)"
    foreach ($RelatifPathsNode in $RelatifPathsNodesArray) {
        if ($hashTable.ContainsKey($RelatifPathsNode) -eq $true){
            $FolderRelativeURL = $FolderRelativeURL.Replace($RelatifPathsNode,$hashTable[$RelatifPathsNode])
        }else{
            $FolderRelativeURL = $FolderRelativeUrl.Replace("/$RelatifPathsNode",'')
        }
    } 

    If($FolderRelativeURL){
        Write-LogDebug -Content "Ensuring Folder '$FolderRelativeURL' Exists from PreprocessFolder function..."
        $Context = RunUploadFolder -Context $Context -FolderRelativeURL $FolderRelativeURL -TargetFolder $TargetFolder -connection $connection
    } 	
    return $Context, $hashTable
}

Function PreprocessFile($Context, $TargetFolder, $SourceFolderPath, $item, $hashTable, $metadata, $connection, $flux, $ContentType, $AllFields){
    $length_Dir = $item.Name.length
    $length_Path = $item.FullName.length
    $RelatifPaths = ($item.FullName).substring(0, $length_Path - $length_Dir - 1).Replace($SourceFolderPath,[string]::Empty)
    $RelatifPathsNodes = $RelatifPaths.Replace("\","/")		
    $RelatifPathsNodesArraySplit = $RelatifPathsNodes.split("/")
    $vide , $RelatifPathsNodesArray= $RelatifPathsNodesArraySplit
    $FolderRelativeUrl = $TargetFolder.ServerRelativeURL +$RelatifPathsNodes

    foreach ($RelatifPathsNode in $RelatifPathsNodesArray) {
        if ($hashTable.ContainsKey($RelatifPathsNode) -eq $true){
            $FolderRelativeUrl = $FolderRelativeUrl.Replace("$RelatifPathsNode",$hashTable["$RelatifPathsNode"])
        }else{
            $FolderRelativeUrl = $FolderRelativeUrl.Replace("/$RelatifPathsNode",'')
        }
    } 

    $lengthCh =$FolderRelativeUrl.Length+$metadata["$($flux.nodeName)"].Length

    if ($lengthCh -gt 400){
        Write-LogDebug -Content "File path that exceeds 400 caraters, length = $($lengthCh) "
        
        $uploadIssueFolder = "$($TargetFolder.ServerRelativeUrl)/Upload Issue Document"
        $Content = Get-PnPFile -Url "$($TargetFolder.ServerRelativeUrl)/Document Upload Issues.csv" -AsString -Connection $connection
    
        $updatedContent = "$($Content) 
        $($hashTable["$($metadata[$flux.dataID])"]), $($FolderRelativeUrl)" 

        $res = Add-PnPFile -Connection $connection -Folder $TargetFolder.ServerRelativeUrl -FileName "Document Upload Issues.csv" -Content $updatedContent 

        $Context = PreprocessVersions -Context $Context -metadata $metadata -item $item -TargetFolder $TargetFolder -connection $connection -FolderRelativeUrl $uploadIssueFolder -flux $flux -normalizedName $hashTable["$($metadata[$flux.dataID])"] -ContentType $ContentType -AllFields $AllFields

    }else{
        $Context = PreprocessVersions -Context $Context -metadata $metadata -item $item -TargetFolder $TargetFolder -connection $connection -FolderRelativeUrl $FolderRelativeUrl -flux $flux -normalizedName $hashTable["$($metadata[$flux.dataID])"] -ContentType $ContentType -AllFields $AllFields
    }

    
    return $Context, $hashTable
}

Function PreprocessVersions($Context, $metadata, $item, $TargetFolder, $connection, $FolderRelativeUrl, $flux, $normalizedName, $ContentType, $AllFields){
	Write-LogDebug -Content "FolderRelativeURL $($FolderRelativeURL)"
	# AEROW ORD START
	$allUploadedFiles = @()
	$uploadedFiles = $item.FullName +"\"+$metadata["$($flux.dataid)"]+"-uploaded.txt"
	if (Test-Path -Path $uploadedFiles -PathType Leaf) {
		$allUploadedFiles = Get-Content $uploadedFiles
	} else {
		#New-Item -Name $uploadedFiles -ItemType File
	}
	
	$allVersions = $metadata['versions']
	if ($metadata['versions'].length -gt 1000) {
		for ($i=-1000; $i -le -1; $i=$i+1 ) {
			$allVersions += $metadata['versions'][$i]
		}
		Write-LogError "Document $($metadata.nodeName) ($($metadata.dataID)) has more than 25 versions, only last 25 ones are retrieved"
	}
	$applyMeta = $true
	foreach ($version in $allVersions) {
	#foreach ($version in $metadata['versions']) {  
	# AEROW ORD END

        $src =  $item.FullName +"\"+$metadata["$($flux.dataid)"]+"-"+ $version.vernum +".dat"
		
		if ($allUploadedFiles -contains $src) {
			Write-LogDebug "Ignoring $($src)"
		} else {
			$versionMeta = $metadata.Clone()
			$versionMeta.Remove('versions')
			$version.PsObject.Members.Remove('created')

			$version | Get-ObjectMember | ForEach-Object { 
				if ($flux.$($_.Key) -ne $null -and $flux.$($_.Key) -ne "") {
					$versionMeta.Add("$($flux.$($_.Key) )", "$($_.Value)" )
				}else{
					$versionMeta.Add("$($_.Key)", "$($_.Value)" )
				}
			}
			
			# AEROW ORD START
			$docSplitOnDot = $normalizedName.Split('.')
			$versionSplitOnDot = $versionMeta["$($flux.filename)"].Split(".")
			# AEROW ORD END
			
			if ( $normalizedName.Split('.').length -eq 1){
				if($versionMeta["$($flux.filename)"].Split(".").length -eq 1){
					if ($flux.mimetypeList."$($versionMeta.mimetype)" -ne $null){
						$FileRelativeURL = $FolderRelativeUrl+"/"+ $normalizedName + "." +  $flux.mimetypeList."$($versionMeta.mimetype)"
					}elseIf ($versionMeta.mimetype -eq "?"){   
						$FileRelativeURL = $FolderRelativeUrl+"/"+ $normalizedName
					}else{
						Write-LogError "The extension $($versionMeta.mimetype) is not managed!"
						 $FileRelativeURL = $FolderRelativeUrl+"/"+ $normalizedName
					}      
				}else {
					$FileRelativeURL = $FolderRelativeUrl+"/"+  $normalizedName + "." + $versionMeta["$($flux.filename)"].Split(".")[-1]
				}
			}else{
				# AEROW ORD START
				if ($docSplitOnDot -lt $versionSplitOnDot) {
					$FileRelativeURL = $FolderRelativeUrl+"/"+  $normalizedName + "." + $versionMeta["$($flux.filename)"].Split(".")[-1]
				} else {
					$FileRelativeURL = $FolderRelativeUrl+"/"+ $normalizedName
				}
				# AEROW ORD END
			} 
			
			$Context = RunUploadFile -Context $Context -SourceFilePath $src -TargetFileURL $FileRelativeURL -TargetFolder $TargetFolder -metadata $versionMeta -connection $connection -applyMetadata $applyMeta -ContentType $ContentType -AllFields $AllFields -FolderRelativeUrl $FolderRelativeUrl -flux $flux.nodeName
			$applyMeta = $false
			Add-Content $uploadedFiles "$($src)"
		}
    } 
    return $Context
}

