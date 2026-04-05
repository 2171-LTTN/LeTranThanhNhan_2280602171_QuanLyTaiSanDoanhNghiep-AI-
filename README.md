# QuanLyTaiSan - Hệ thống Quản lý Tài sản

## Yêu cầu

- **Java** 17+
- **Maven** 3.8+
- **Node.js** 18+
- **MongoDB** 6.0+

## Cài đặt

### 1. Backend
```bash
cd backend
mvn clean install
```

### 2. Frontend
```bash
cd frontend
npm install
```

## Chạy ứng dụng

### Terminal 1 - Backend (cổng 8080)
```bash
cd backend
mvn spring-boot:run
```

### Terminal 2 - Frontend (cổng 5173)
```bash
cd frontend
npm run dev
```

## Đăng nhập

- **Admin:** `admin@company.com` / `Admin@123`
- **User:** `mai.tran@company.com` / `password123`

## Database

Kết nối MongoDB: `mongodb://localhost:27017/quanlytaisan`

## File dữ liệu

- `exported-data.json` - Dữ liệu mẫu (đã xuất từ MongoDB)
