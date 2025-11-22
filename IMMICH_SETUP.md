# Immich Photo Backup Setup Guide

## What is Immich?

Immich is a self-hosted photo and video backup solution - like Google Photos but you own your data. It runs on your Raspberry Pi and provides automatic photo organization, face recognition, and mobile backup.

## Server Setup on Raspberry Pi 5

### Option 1: Docker Compose (Recommended)

1. **Install Docker** (if not already installed):
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
```

2. **Create Immich directory**:
```bash
mkdir -p ~/immich
cd ~/immich
```

3. **Download docker-compose.yml**:
```bash
wget https://github.com/immich-app/immich/releases/latest/download/docker-compose.yml
wget https://github.com/immich-app/immich/releases/latest/download/example.env -O .env
```

4. **Edit .env file**:
```bash
nano .env
```

Change:
- `UPLOAD_LOCATION=/path/to/photos` (e.g., `/home/pi/Photos`)
- `DB_PASSWORD=your_secure_password`

5. **Start Immich**:
```bash
docker-compose up -d
```

6. **Access Immich**:
- Open browser: `http://raspberrypi.local:2283`
- Create admin account
- Complete setup wizard

### Option 2: Manual Installation

See: https://immich.app/docs/install/environment-variables

## App Configuration

### 1. Get API Key

1. Open Immich web interface: `http://raspberrypi.local:2283`
2. Login with admin account
3. Go to **Account Settings** → **API Keys**
4. Click **New API Key**
5. Name it "Android App"
6. Copy the generated API key

### 2. Configure App

In the Photos tab:
- **Server URL**: `http://raspberrypi.local:2283` (or your Pi's IP: `http://10.0.0.x:2283`)
- **API Key**: Paste the key from step 1
- Click **Connect**

### 3. Upload Photos

1. Click **Select Photos**
2. Choose photos from your gallery
3. Click **Upload Selected Photos**
4. Monitor progress in Status section

### 4. Auto Backup (Optional)

- Enable **Auto Backup (WiFi only)** switch
- Photos will automatically backup when connected to WiFi
- Runs in background

## Immich Features

✅ **Photo Organization**: Automatic albums by date, location
✅ **Face Recognition**: AI-powered face detection
✅ **Search**: Search by objects, locations, people
✅ **Sharing**: Share albums with family
✅ **Timeline**: Browse photos by date
✅ **Duplicates**: Automatic duplicate detection
✅ **RAW Support**: DSLR raw file support
✅ **Video Support**: Backup videos too
✅ **Mobile Access**: Web interface works on mobile
✅ **No Cloud**: Everything stays on your Pi

## API Endpoints Used

- **Ping**: `GET /api/server-info/ping` - Test connection
- **Upload**: `POST /api/asset/upload` - Upload photo
- **Authentication**: `x-api-key` header

## Storage Recommendations

- **SD Card**: Minimum 128GB (photos grow quickly!)
- **External HDD/SSD**: Recommended for large libraries
- **NAS**: Mount network storage for unlimited space

Example mount external drive:
```bash
sudo mkdir /mnt/photos
sudo mount /dev/sda1 /mnt/photos
# Set UPLOAD_LOCATION=/mnt/photos in .env
```

## Troubleshooting

**Connection Failed:**
- Check Immich is running: `docker ps`
- Verify port 2283 is accessible
- Try IP address instead of hostname
- Check firewall: `sudo ufw allow 2283`

**Upload Failed:**
- Verify API key is correct
- Check storage space on Pi
- Ensure app has photo permissions (Android settings)

**Slow Upload:**
- Use WiFi instead of mobile data
- Reduce photo quality in camera settings
- Upload in smaller batches

## Security Recommendations

1. **HTTPS**: Use reverse proxy (nginx) with SSL certificate
2. **Firewall**: Only allow access from local network
3. **Strong Password**: Use complex admin password
4. **API Keys**: Create separate keys for different devices
5. **Backup**: Backup your photos regularly to external drive

## Resource Usage

Raspberry Pi 5:
- RAM: ~1-2GB (with face recognition)
- CPU: Low during idle, spikes during ML tasks
- Storage: Depends on photo library size

## Useful Commands

```bash
# View logs
docker-compose logs -f immich-server

# Restart Immich
docker-compose restart

# Update Immich
docker-compose pull
docker-compose up -d

# Check status
docker-compose ps

# Stop Immich
docker-compose down

# Backup database
docker exec -t immich-postgres pg_dump -U postgres immich > backup.sql
```

## Links

- Official Site: https://immich.app
- Documentation: https://immich.app/docs
- GitHub: https://github.com/immich-app/immich
- Discord: https://discord.gg/immich

## Next Steps

1. ✅ Install Immich on Raspberry Pi
2. ✅ Create admin account
3. ✅ Generate API key
4. ✅ Configure app
5. ✅ Test upload with few photos
6. ✅ Enable auto backup
7. ✅ Enjoy your self-hosted photo backup! 📸
