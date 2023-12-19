Function LoopCommunity($Context, $SourceFolderPath, $hashTable, $connection, $NewSiteName, $flux)
{
    $Error.Clear()
    $nbFiles = 0
    $nbFilesReal = 0
    $Folder = $null
    $LibraryName = "Documents"
    $Context, $Folder = loadFolder -Context $Context -LibraryName $LibraryName 
	
	# AEROW ORD Compute community elements here
	$ContentType = Get-PnPContentType -Identity "CHORUS Files"
	$AllFields = Get-PnPProperty -ClientObject $ContentType -property "Fields" -Connection $connection

    Get-ChildItem  $SourceFolderPath -Recurse | ForEach-Object {
        $metadata = @{}
		$SourceFolderRelativeURL = $SourceFolderPath+$_.FullName.Replace($SourceFolderPath,[string]::Empty)
		$file = "$SourceFolderRelativeURL\$($_.Name).json"
        
            
        if (Test-Path $file -PathType leaf)
        {
            $metadata = GetMetadata -file $file -metadata $metadata -flux $flux
            $subtype = $metadata["subtype"]

            #Community node
            if ($subtype -eq 3030202){  
                $file = "$SourceFolderRelativeURL\$($_.Name)"+"_categories.json"
				Write-LogDebug -Content "Prepare UpdateSiteMetadata"
                if (Test-Path $file -PathType leaf){
                    $listName = "Identity Card"
                    $siteMetadata = @{}
                    $siteMetadata.add("Title","$($NewSiteName)")
                    $siteMetadata = GetMetadata -file $file -metadata $siteMetadata -flux $flux
					#Write-LogDebug -Content "Launch UpdateSiteMetadata"
                    #UpdateSiteMetadata -listName $listName -siteMetadata $siteMetadata -connection $connection
                }

                #Create TTOL Permissions Folder, upload the 2 acls role files, Upload Issue Document
                Try
                {
                    #$permissionFolder = $Folder.ServerRelativeURL + "/TTOL Permissions"
                    #Write-LogDebug -Content "Ensuring Folder '$permissionFolder' Exists for permissionFolder..."
                    #$Context = RunUploadFolder -Context $Context -FolderRelativeURL $permissionFolder -TargetFolder $Folder -connection $connection

                    #$uploadIssueFolder = $Folder.ServerRelativeURL + "/Upload Issue Document"
                    #Write-LogDebug -Content "Ensuring Folder '$uploadIssueFolder' Exists for uploadIssueFolder..."
                    #$Context = RunUploadFolder -Context $Context -FolderRelativeURL $uploadIssueFolder -TargetFolder $Folder -connection $connection
                    #$res = Add-PnPFile -Connection $connection -Folder $Folder.ServerRelativeUrl -FileName "Document Upload Issues.csv" -Content "FILE NAME, ORIGINAL PATH" 

                    #$src = $SourceFolderPath +"/$($_.FullName.Replace($SourceFolderPath,[string]::Empty).Split('-')[-1])_acl.csv"
                    #$TargetFileURL = $permissionFolder +"/roles_membres.csv"
                    #$Context = RunUploadFile -Context $Context -SourceFilePath $src -TargetFileURL $TargetFileURL -TargetFolder $TargetFolder -metadata @{} -connection $connection -applyMetadata $false -ContentType $ContentType -AllFields $AllFields -FolderRelativeUrl $permissionFolder -flux "roles_membres.csv"
                
                    #$src = "$SourceFolderRelativeURL/$($_.Name)_acl.json"
                    #$TargetFileURL = $permissionFolder +"/roles_description.txt"
                    #$Context = RunUploadFile -Context $Context -SourceFilePath $src -TargetFileURL $TargetFileURL -TargetFolder $TargetFolder -metadata @{} -connection $connection -applyMetadata $false -ContentType $ContentType -AllFields $AllFields -FolderRelativeUrl $permissionFolder -flux "roles_membres.csv"
                }Catch
                {
                    Write-LogError "Failed to preprocess the folder $($permissionFolder) : $( $_.Exception)"
                }
                
            }# Folders
            ElseIf ($subtype -eq 3030329 -or $subtype -eq 3030331 -or $subtype -eq 0 -or $subtype -eq 136 -or  $subtype -eq 751 -or  $subtype -eq 5573){
                Try
                {
                    $hashTable = UpdateHashTable -hashTable $hashTable -oldName $_.Name -newName $metadata["$($flux.nodeName)"]
                    $Context, $hashTable = PreprocessFolder -Context $Context -TargetFolder $Folder -SourceFolderPath $SourceFolderPath -item $_ -hashTable $hashTable -connection $connection
                }Catch
                {
                    Write-LogError "Failed to preprocess the folder $($_.Name) : $( $_.Exception)"
                }

                if($Error.Count -ne 0)
                {
                    Write-LogError "$($Error)" 
                   
                }
            }#Files
            ElseIf ($subtype -eq 144  -or $subtype -eq 5574 -or $subtype -eq 749 ){
                Try
                {
                    $file = "$SourceFolderRelativeURL\$($_.Name)"+"_categories.json"

                    if (Test-Path $file -PathType leaf){
                        $metadata = GetMetadata -file $file -metadata $metadata -flux $flux
                    }

                    $hashTable = UpdateHashTable -hashTable $hashTable -oldName $_.Name -newName $metadata["$($flux.nodeName)"] 
                    $Context, $hashTable = PreprocessFile -Context $Context -TargetFolder $Folder -SourceFolderPath $SourceFolderPath -item $_ -hashTable $hashTable -metadata $metadata -connection $connection -flux $flux -ContentType $ContentType -AllFields $AllFields
                    
                    if($Error.Count -ne 0)
                    {
                        Write-LogError "$($Error)" 
                   
                    }else{
                        $nbFiles = $nbFiles + 1
                    }
                    
                }Catch{
                    Write-LogError "Failed to preprocess the file $($_.Name) : $( $_.Exception)"
                }

                $nbFilesReal = $nbFilesReal + 1 
            }
        }  
    }
    Write-LogInfo -Content "Total number of uploaded files in this community : $($nbFiles)/$($nbFilesReal)"

    return 
}