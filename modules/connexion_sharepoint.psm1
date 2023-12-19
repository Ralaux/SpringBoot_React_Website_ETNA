#INITIALISATION
Function InitConnexion_old(){

    $WebUrl = "https://poleose.sharepoint.com/sites/SAS-PRJ-THA-NEWFARMSIXT2TolPr-Etude"
    $flux = Get-Content "C:\Users\MariaSOLOMON\OneDrive - AEROW SAS\Bureau\work\Mission_thales_nest\Injection\injectToSharepoint\login1.txt" 
    
    $AdminName = $flux[0]
    $AdminPassword = $flux[1]
    
    #Setup Credentials to connect
    $MyCredential = New-Object Microsoft.SharePoint.Client.SharePointOnlineCredentials($AdminName,(ConvertTo-SecureString $AdminPassword -AsPlainText -Force))
    $Context = New-Object Microsoft.SharePoint.Client.ClientContext($WebUrl)
    $Context.Credentials = $MyCredential
    Write-Host "End InitConnexion"
    return $Context
}

Function InitConnexion($TenantUrl, $appid, $appsecret){

    Try
    {
        $connection = Connect-PnPOnline -Url $TenantUrl -ClientId $appid -ClientSecret $appsecret -WarningAction Ignore -ErrorAction Stop -ReturnConnection
        $Context = Get-PnPContext
#		-Connection $connection
    }
    Catch
    {
        Write-LogError "Failed to connect to $($TenantUrl) : $($_.Exception)"
        Write-LogError "Exiting...."
        Break script
    }

    Write-LogInfo -Content "Connected to to tenant : $TenantUrl"

    return $Context, $connection
}

Function loadFolder($Context, $LibraryName){
    Try
    {
        $List = $Context.Web.Lists.GetByTitle($LibraryName)

        $Folder = $List.RootFolder 
        $Context.Load($Folder)
        $Context.ExecuteQuery()
    }
    Catch
    {
        Write-LogError "Failed to load the Documents : $($_.Exception)"
        Write-LogError "Exiting...."
        Break script
    }
    
    Write-LogInfo -Content "The remote folder $($Folder.ServerRelativeURL) is loaded "
    
    return $Context, $Folder
}