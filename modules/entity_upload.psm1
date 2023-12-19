#Execute small file upload request
Function StandartFile_Upload($Context, $TargetFileURL,$SourceFilePath, $metadata, $connection, $FolderRelativeUrl, $flux){
    
    try{
        $contentType = Get-PnPContentType -Identity "ETOL Files" -Connection $connection
    }catch{
		Write-LogError "Failed to upload the standard file $($TargetFileURL), the content type cannot be retrieved : $( $_.Exception)"
        Write-LogError "Exiting...."
	}
    Write-LogDebug -Content "SourceFileURL  $($SourceFilePath)"
    Write-LogDebug -Content "TargetFileURL  $($FolderRelativeUrl) from StandartFile_Upload"

    try{	
        $metadata = ValidateFileMetadata -metadata $metadata -contentType $contentType -Connection $connection -flux $flux
        $newFileName = $TargetFileURL.split("/")[-1]
        if ($newFileName.Substring(0,1) -eq " "){
            $newFileName = $newFileName.Substring(1)
        }
		Add-PnPFile -Connection $connection -Path $SourceFilePath -Folder $FolderRelativeUrl -NewFileName $newFileName -ContentType $contentType -Values $metadata
	}catch{
		Write-LogError "Failed to upload the standard file $($TargetFileURL), the file cannot be added to the target folder : $( $_.Exception)"
        Write-LogError "Exiting...."
	}
    $Context = Get-PnPContext
#	-Connection $connection

    if($Error.Count -ne 0)
    {
        Write-LogError "$($Error)" 
                   
    }
    return $Context
}

#Execute chunk by chunk file upload
Function LargeFile_Upload($Context, $TargetFileURL, $TargetFolder, $FileSize, $SourceFilePath, $BlockSize, $metadata, $connection, $flux, $applyMetadata, $ContentType, $AllFields){

    Write-LogDebug -Content "SourceFileURL  $($SourceFilePath)"
    Write-LogDebug -Content "TargetFileURL  $($TargetFileURL) from LargeFile_Upload"

    # Each block upload requires a unique ID.
    $UploadId = [GUID]::NewGuid()
    $BytesUploaded = $null
    $Fs = $null
    $FileSizeInMB = [math]::Round($(($FileSize/1024)/1014),2)

    $Context = Get-PnPContext
#	-Connection $connection
 
    Try
    {
        $Fs = [System.IO.File]::Open($SourceFilePath, [System.IO.FileMode]::Open, [System.IO.FileAccess]::Read, [System.IO.FileShare]::ReadWrite)
        $br = New-Object System.IO.BinaryReader($Fs)
        $buffer = New-Object System.Byte[]($BlockSize)
        $lastBuffer = $null
        $fileoffset = 0
        $totalBytesRead = 0
        $first = $true
        $last = $false

        # Read data from file system in blocks. 
        while(($bytesRead = $br.Read($buffer, 0, $buffer.Length)) -gt 0)
        {
            $totalBytesRead = $totalBytesRead + $bytesRead
            # You've reached the end of the file.
            if($totalBytesRead -eq $FileSize)
            {
                $last = $true
                # Copy to a new buffer that has the correct size.
                $lastBuffer = New-Object System.Byte[]($bytesRead)
                [array]::Copy($buffer, 0, $lastBuffer, 0, $bytesRead)
            }
            
            try
            {
                #Show upload file progress
                $UploadSizeInBM = [math]::Round($(($totalBytesRead/1024)/1014),2)
                Write-Progress -activity "Uploading file $FileName" -status "$UploadSizeInBM out of $FileSizeInMB MB uploaded" -PercentComplete (($UploadSizeInBM*100)/$FileSizeInMB) 
            }
            Catch{}
        
            If($first)
            {
                $FileStream = New-Object System.IO.MemoryStream
                # Add an empty file

                $FileCreationInfo = New-Object Microsoft.SharePoint.Client.FileCreationInformation
                $FileCreationInfo.Overwrite = $true
                $FileCreationInfo.ContentStream = $FileStream
                $FileCreationInfo.URL = $TargetFileURL
            
                try{	
                    $Upload = $TargetFolder.Files.Add($FileCreationInfo)
                }catch{
                    Write-LogError "Failed to upload the file $($TargetFileURL) by chunk, the file cannot be added to the target folder : $( $_.Exception)"
                    Write-LogError "Exiting...."
                }
                $Context.Load($Upload)
                # Start upload by uploading the first slice.                    
                $s = [System.IO.MemoryStream]::new($buffer) 
                
                # Call the start upload method on the first slice.
                $BytesUploaded = $Upload.StartUpload($UploadId, $s)

                $Context.ExecuteQuery()   
                                    
                # fileoffset is the pointer where the next slice will be added.
                $fileoffset = $BytesUploaded.Value
                                    
                # You can only start the upload once.
                $first = $false
				
				# If there is only one loop (small file)
				If($last) {
                                        
                    # Is this the last slice of data?
                    $s = [System.IO.MemoryStream]::new($lastBuffer)                        

                    # End sliced upload by calling FinishUpload.
                    $Upload = $Upload.FinishUpload($UploadId, $fileoffset, $s)
                    
                    $Context.ExecuteQuery()

                    $Upload = $Context.Web.GetFileByServerRelativeUrl($TargetFileURL)
					if ($applyMetadata) {
						$Context, $Upload = UpdateVersionMetadata -Context $Context -file $Upload -metadata $metadata -connection $connection -flux $flux -ContentType $ContentType -AllFields $AllFields
					}
                
                    # Large file upload complete. Return the file object for the uploaded file.
                    return $Context                    
                }
            
            }
            Else
            {
            # Get a reference to your file.
                $Upload = $Context.Web.GetFileByServerRelativeUrl($TargetFileURL)
                                
                If($last) {
                                        
                    # Is this the last slice of data?
                    $s = [System.IO.MemoryStream]::new($lastBuffer)                        

                    # End sliced upload by calling FinishUpload.
                    $Upload = $Upload.FinishUpload($UploadId, $fileoffset, $s)
                    
                    $Context.ExecuteQuery()

                    $Upload = $Context.Web.GetFileByServerRelativeUrl($TargetFileURL)
					if ($applyMetadata) {
						$Context, $Upload = UpdateVersionMetadata -Context $Context -file $Upload -metadata $metadata -connection $connection -flux $flux -ContentType $ContentType -AllFields $AllFields
					}
                
                    # Large file upload complete. Return the file object for the uploaded file.
                    return $Context                    
                }
                else
                {                        
                    $s = [System.IO.MemoryStream]::new($buffer)
                                            
                    # Continue sliced upload.
                    $BytesUploaded = $Upload.ContinueUpload($UploadId, $fileoffset, $s)
                    $Context.ExecuteQuery()
                                            
                    # Update fileoffset for the next slice.
                    $fileoffset = $BytesUploaded.Value
                                        
                }
            }		
        } 
    }
    Catch
    {
        Write-LogError "Failed to upload the file $($TargetFileURL) by chunk : $( $_.Exception)"
        Write-LogError "Exiting...."
    }
    Finally {        
        if ($Fs -ne $null){
            $Fs.Dispose()
        }		
    }

    if($Error.Count -ne 0)
    {
        Write-LogError "$($Error)" 
                   
    }

    return $Context
}

#Function to Upload a File to a SharePoint Online
Function RunUploadFile($Context, $SourceFilePath, $TargetFileURL, $TargetFolder, $FileChunkSizeInMB=5, $metadata, $connection, $FolderRelativeUrl, $flux, $applyMetadata, $ContentType, $AllFields)   
{
	# Calculate block size in bytes.
	$BlockSize = $FileChunkSizeInMB * 1024 * 1024
    # Get the size of the file.
	$FileSize = (Get-Item $SourceFilePath).length
    if ($FileSize -le $BlockSize)
	{
        Try {
            #$Context = StandartFile_Upload -Context $Context -TargetFileURL $TargetFileURL -FolderRelativeUrl $FolderRelativeUrl -SourceFilePath $SourceFilePath -metadata $metadata -connection $connection -flux $flux
			$Context = LargeFile_Upload -Context $Context -TargetFileURL $TargetFileURL -TargetFolder $TargetFolder -FileSize $FileSize -SourceFilePath $SourceFilePath -BlockSize $BlockSize -metadata $metadata -connection $connection -flux $flux -applyMetadata $applyMetadata -ContentType $ContentType -AllFields $AllFields
            Write-LogInfo -Content "File '$TargetFileURL' Uploaded Successfully! $nbFiles"
        }
        Catch {
             Write-LogError "Failed to upload the file $($TargetFileURL) : $( $_.Exception)"
        }
    
    }else{  
        Try {
            $Context = LargeFile_Upload -Context $Context -TargetFileURL $TargetFileURL -TargetFolder $TargetFolder -FileSize $FileSize -SourceFilePath $SourceFilePath -BlockSize  $BlockSize -metadata $metadata -connection $connection -flux $flux -applyMetadata $applyMetadata -ContentType $ContentType -AllFields $AllFields
	        Write-LogInfo -Content "File '$TargetFileURL' Uploaded Successfully! $nbFiles"
        }
        Catch {
            Write-LogError "Failed to upload the file $($TargetFileURL) : $( $_.Exception)"
        }
    }
   
    return $Context
}

#Function to Check if Folder Exists. If not, Create the Folder
Function RunUploadFolder($Context, $FolderRelativeURL, $connection)
{
    $Context = Get-PnPContext
	#-Connection $connection
	#Check Folder Exists
    Try {
        $Folder = $Context.Web.GetFolderByServerRelativeUrl($FolderRelativeURL)
        $Context.Load($Folder)
        $Context.ExecuteQuery()
  
        Write-LogInfo -Content "The folder $($FolderRelativeURL) already exists!"
    }
    Catch {
        #Create New Sub-Folder
        $Folder = $Context.Web.Folders.Add($FolderRelativeURL)
        $Context.ExecuteQuery()
		
        Write-LogInfo -Content "Created Folder at $FolderRelativeURL"
    }

    if($Error.Count -ne 0)
    {
        Write-LogError "$($Error)" 
                   
    }
    return $Context
}
