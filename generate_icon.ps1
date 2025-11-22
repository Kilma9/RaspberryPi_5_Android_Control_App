# Android Icon Generator PowerShell Script
Add-Type -AssemblyName System.Drawing

$iconPath = "C:\Users\Kuba\Desktop\ICON_android.jpg"
$outputBase = "C:\Users\Kuba\Desktop\Kilma_Android_App\app\src\main\res"

$sizes = @{
    'mipmap-mdpi' = 48
    'mipmap-hdpi' = 72
    'mipmap-xhdpi' = 96
    'mipmap-xxhdpi' = 144
    'mipmap-xxxhdpi' = 192
}

try {
    $originalImage = [System.Drawing.Image]::FromFile($iconPath)
    Write-Host "Original icon size: $($originalImage.Width)x$($originalImage.Height)"
    
    foreach ($folder in $sizes.Keys) {
        $size = $sizes[$folder]
        $outputDir = Join-Path $outputBase $folder
        
        if (!(Test-Path $outputDir)) {
            New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
        }
        
        $resized = New-Object System.Drawing.Bitmap($size, $size)
        $graphics = [System.Drawing.Graphics]::FromImage($resized)
        $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
        $graphics.DrawImage($originalImage, 0, 0, $size, $size)
        
        $outputFile = Join-Path $outputDir "ic_launcher.png"
        $resized.Save($outputFile, [System.Drawing.Imaging.ImageFormat]::Png)
        Write-Host "Created: $folder\ic_launcher.png (${size}x${size})"
        
        $outputFileRound = Join-Path $outputDir "ic_launcher_round.png"
        $resized.Save($outputFileRound, [System.Drawing.Imaging.ImageFormat]::Png)
        Write-Host "Created: $folder\ic_launcher_round.png (${size}x${size})"
        
        $graphics.Dispose()
        $resized.Dispose()
    }
    
    $originalImage.Dispose()
    Write-Host "`nIcon generation complete!"
    Write-Host "Now rebuild your app with: .\gradlew assembleDebug"
} catch {
    Write-Host "Error: $_"
}
