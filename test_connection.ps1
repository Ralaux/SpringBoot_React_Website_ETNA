[System.Reflection.Assembly]::LoadWithPartialName("Microsoft.SharePoint.Client")
[System.Reflection.Assembly]::LoadWithPartialName("Microsoft.SharePoint.Client.Runtime")

[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
Add-Type @"
    using System.Net;
    using System.Security.Cryptography.X509Certificates;
    public class TrustAllCertsPolicy : ICertificatePolicy {
        public bool CheckValidationResult(
            ServicePoint srvPoint, X509Certificate certificate,
            WebRequest request, int certificateProblem) {
            return true;
        }
    }
"@
[System.Net.ServicePointManager]::CertificatePolicy = New-Object TrustAllCertsPolicy

Import-Module ./modules/general_functions.psm1
Import-Module ./modules/connexion_sharepoint.psm1
Import-Module ./modules/launch_injection.psm1
Import-Module ./modules/entity_upload.psm1
Import-Module ./modules/metadata_process.psm1
Import-Module ./modules/entity_process.psm1


$flux = Get-Content "./config_test_connection.json" | ConvertFrom-Json

Write-LogStart

if ($flux.debugMode -eq $false){
    $global:debug = $false
}elseIf ($flux.debugMode -eq $true){
    $global:debug = $true
}else{
    Write-LogError "Unknown debug mode!"
}

$TenantUrl = $flux.tenantUrl
$TenantUser = $flux.tenantUser
$SourceFolderPath=$flux.sourceFolderPath
$storagequota = $flux.storageQuota
$Cred = Get-Credential -UserName $TenantUser -Message 'Enter Password'

Write-LogInfo -Content "LogFile: $global:LogFile"


Try
{
    Write-LogDebug -Content "Connecting to tenant : $TenantUrl"
    $startdate=Get-Date
    $connection = Connect-PnPOnline -Url $TenantUrl -Credential $Cred -WarningAction Ignore -ErrorAction Continue -ReturnConnection
    $item1 = Get-PnPContentTypePublishingHubUrl -Connection $connection -WarningAction Ignore -ErrorAction Continue
    Get-Time -startdate $startdate -mode $Global:debug -content "Connect-pnponline :"
    Write-LogDebug -Content "Connected to tenant : $TenantUrl"
    Write-LogDebug -Content "Connexion : $connection"
}
Catch
{
    Write-LogError "Exception : $( $_.Exception)"
    Write-LogError "Failed to connect : Exiting"
}