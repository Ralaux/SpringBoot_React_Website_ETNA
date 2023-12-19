$global:csvDelimiter = ";"
$global:csvDelimiter1 = ","
$today = Get-Date -Format "yyyyMMdd_HHmm"
$sLogFile = $today+"_injection.log"
$global:LogFile= Join-Path -Path "./logs/" -ChildPath $sLogFile

Function Get-Time{
[CmdletBinding()]
	Param (
		[parameter(mandatory=$true)][DateTime]$startdate,
		[parameter(mandatory=$true)][string]$content,
		[parameter(mandatory=$true)][string]$mode
	)
	Process
	{
        $now = [DateTime]::Now
        $delta = $now-$startdate

        $sdelta = " in "
        if($delta.Hours -gt 0)
        {
            $sdelta += "$($delta.Hours) Hours "
            $sdelta += "$($delta.Minutes) Minutes "
            $sdelta += "$($delta.Seconds) Seconds"
        }
        else
        {
            if($delta.Minutes -gt 0)
            {
                $sdelta += "$($delta.Minutes) Minutes "
                $sdelta += "$($delta.Seconds) Seconds"
            }
            else
            {
                $sdelta += "$($delta.Seconds).$($delta.Milliseconds) Seconds"
            }

        }
        
        $ss = $content + $sdelta
   
        
        if($mode -eq "DEBUG")
        {
            Write-LogDebug $ss
        }
        else
        {
            Write-LogInfo $ss
        }

    }
}



Function Write-Log{
	[CmdletBinding()]
	Param (
		[parameter(mandatory=$true)][string]$mode,
		[parameter(mandatory=$true)][string]$LineValue
	)
	Process
	{
        $InfoColor = "Green"
        $ErrorColor= "Red"
        $DebugColor = "Yellow" 
        $defaultFGColor = $host.UI.RawUI.ForegroundColor
        if($defaultFGColor -eq "Black")
        {
            $InfoColor = "Blue"
            $ErrorColor= "Red"
            $DebugColor = "Magenta"
        }

        $LineValue =$([DateTime]::Now).tostring() +"[$mode]" + $LineValue
        If ($mode -eq "DEBUG")
		{
            if($global:debug)
			{
                write-host  -ForegroundColor $DebugColor $LineValue
                Add-Content $global:LogFile $LineValue 
			}
        }
        else
        {
            If ($mode -eq "INFO ")
			{
				write-host  -ForegroundColor $InfoColor $LineValue
			}
			else
			{
				write-host  -ForegroundColor $ErrorColor $LineValue
			}
            Add-Content $LogFile $LineValue
        }
    }
 }
 
 Function Write-LogInfo{
[CmdletBinding()]
	Param (
		[parameter(mandatory=$true)][string]$Content
	)
	Process
	{
        Write-Log -mode "INFO " -LineValue $Content
    }
}
Function Write-LogDebug{
[CmdletBinding()]
	Param (
		[parameter(mandatory=$true)][string]$Content
	)
	Process
	{
        Write-Log -mode "DEBUG" -LineValue $Content
    }
}
Function Write-LogError{
[CmdletBinding()]
	Param (
		[parameter(mandatory=$true)][string]$Content
	)
	Process
	{
        Write-Log -mode "ERROR" -LineValue $Content
    }
}
Function Write-LogStart{  
    Process
	{
        if (!(Test-Path $global:LogFile))
        {
            New-Item $global:LogFile -ItemType file
        }
        Write-LogInfo -Content "***************************************************************************************************"
        Write-LogInfo -Content " Started processing at [$([DateTime]::Now)]."
        Write-LogInfo -Content "***************************************************************************************************"
    }
}
Function Write-LogFinish{  
    Process
	{
        Write-LogInfo -Content "***************************************************************************************************"
        Write-LogInfo -Content " End processing at [$([DateTime]::Now)]."
        Write-LogInfo -Content "***************************************************************************************************"
    }
}
function Export-CSVAppend {
[CmdletBinding(DefaultParameterSetName='Delimiter',
  SupportsShouldProcess=$true, ConfirmImpact='Medium')]
param(
 [Parameter(Mandatory=$true, ValueFromPipeline=$true,
           ValueFromPipelineByPropertyName=$true)]
 [System.Management.Automation.PSObject]
 ${InputObject},

 [Parameter(Mandatory=$true, Position=0)]
 [Alias('PSPath')]
 [System.String]
 ${Path},
 
 #region -Append (added by Dmitry Sotnikov)
 [Switch]
 ${Append},
 #endregion 

 [Switch]
 ${Force},

 [Switch]
 ${NoClobber},

 [ValidateSet('Unicode','UTF7','UTF8','ASCII','UTF32',
                  'BigEndianUnicode','Default','OEM')]
 [System.String]
 ${Encoding},

 [Parameter(ParameterSetName='Delimiter', Position=1)]
 [ValidateNotNull()]
 [System.Char]
 ${Delimiter},

 [Parameter(ParameterSetName='UseCulture')]
 [Switch]
 ${UseCulture},

 [Alias('NTI')]
 [Switch]
 ${NoTypeInformation})

begin
{
 # This variable will tell us whether we actually need to append
 # to existing file
 $AppendMode = $false
 
 try {
  $outBuffer = $null
  if ($PSBoundParameters.TryGetValue('OutBuffer', [ref]$outBuffer))
  {
      $PSBoundParameters['OutBuffer'] = 1
  }
  $wrappedCmd = $ExecutionContext.InvokeCommand.GetCommand('Export-Csv',
    [System.Management.Automation.CommandTypes]::Cmdlet)
        
        
 #String variable to become the target command line
 $scriptCmdPipeline = ''

 # Add new parameter handling
 #region Dmitry: Process and remove the Append parameter if it is present
 if ($Append) {
  
  $PSBoundParameters.Remove('Append') | Out-Null
    
  if ($Path) {
   if (Test-Path $Path) {        
    # Need to construct new command line
    $AppendMode = $true
    
    if ($Encoding.Length -eq 0) {
     # ASCII is default encoding for Export-CSV
     $Encoding = 'ASCII'
    }
    
    # For Append we use ConvertTo-CSV instead of Export
    $scriptCmdPipeline += 'ConvertTo-Csv -NoTypeInformation '
    
    # Inherit other CSV convertion parameters
    if ( $UseCulture ) {
     $scriptCmdPipeline += ' -UseCulture '
    }
    if ( $Delimiter ) {
     $scriptCmdPipeline += " -Delimiter '$Delimiter' "
    } 
    
    # Skip the first line (the one with the property names) 
    $scriptCmdPipeline += ' | Foreach-Object {$start=$true}'
    $scriptCmdPipeline += '{if ($start) {$start=$false} else {$_}} '
    
    # Add file output
    $scriptCmdPipeline += " | Out-File -FilePath '$Path'"
    $scriptCmdPipeline += " -Encoding '$Encoding' -Append "
    
    if ($Force) {
     $scriptCmdPipeline += ' -Force'
    }

    if ($NoClobber) {
     $scriptCmdPipeline += ' -NoClobber'
    }   
   }
  }
 } 
  

  
 $scriptCmd = {& $wrappedCmd @PSBoundParameters }
 
 if ( $AppendMode ) {
  # redefine command line
  $scriptCmd = $ExecutionContext.InvokeCommand.NewScriptBlock(
      $scriptCmdPipeline
    )
 } else {
  # execute Export-CSV as we got it because
  # either -Append is missing or file does not exist
  $scriptCmd = $ExecutionContext.InvokeCommand.NewScriptBlock(
      [string]$scriptCmd
    )
 }

 # standard pipeline initialization
 $steppablePipeline = $scriptCmd.GetSteppablePipeline(
        $myInvocation.CommandOrigin)
 $steppablePipeline.Begin($PSCmdlet)
 
 } catch {
   throw
 }
    
}

process
{
  try {
      $steppablePipeline.Process($_)
  } catch {
      throw
  }
}

end
{
  try {
      $steppablePipeline.End()
  } catch {
      throw
  }
}

}
