# 🌐  Mạng Xã Hội Okayji

> **Okayji** là một nền tảng mạng xã hội hiện đại, nơi người dùng có thể kết nối, chia sẻ bài viết, trò chuyện trực tiếp và tương tác cùng cộng đồng. Hệ thống tích hợp kiểm duyệt nội dung tự động bằng AI nhằm đảm bảo môi trường lành mạnh và an toàn.

---

## 📋 Mục lục

- [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
- [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
- [Các chức năng chính](#-các-chức-năng-chính)
- [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
- [Hướng dẫn cài đặt và chạy](#-hướng-dẫn-cài-đặt-và-chạy)

---

## 🛠 Công nghệ sử dụng

### Back-end

| Công nghệ | Phiên bản | Mô tả |
|---|---|---|
| Java | 21 | Ngôn ngữ lập trình chính |
| Spring Boot | 4.0.2 | Framework phát triển REST API |
| Spring Security | (theo Spring Boot) | Xác thực & phân quyền |
| Spring Data JPA | (theo Spring Boot) | ORM, tương tác cơ sở dữ liệu |
| Spring WebSocket | (theo Spring Boot) | Nhắn tin & thông báo real-time |
| Spring AI | 2.0.0-M2 | Tích hợp OpenAI để kiểm duyệt nội dung |
| MySQL Connector | (theo Spring Boot) | Driver kết nối MySQL |
| JWT (jjwt) | 0.12.6 | Tạo & xác thực JSON Web Token |
| AWS SDK S3 | 2.41.28 | Lưu trữ ảnh & video trên AWS S3 |
| MapStruct | 1.5.5.Final | Ánh xạ DTO ↔ Entity |
| Lombok | (theo Spring Boot) | Giảm boilerplate code |
| Springdoc OpenAPI | 2.7.0 | Tự động sinh tài liệu API (Swagger UI) |
| Maven | 3.x | Quản lý dependency & build |

### Cơ sở dữ liệu

| Công nghệ | Mô tả |
|---|---|
| MySQL 9.3.x | Hệ quản trị cơ sở dữ liệu quan hệ chính |

### Dịch vụ ngoài

| Dịch vụ | Mục đích |
|---|---|
| AWS S3 | Lưu trữ ảnh, video người dùng đăng tải |
| OpenAI API | Kiểm duyệt nội dung bài viết / bình luận tự động |
| FFmpeg | Xử lý, chuyển đổi định dạng video |

---

## 🏗 Kiến trúc hệ thống

```
┌─────────────────────────────────────────┐
│               Client                    │
└──────────────────┬──────────────────────┘
                   │ HTTP REST / WebSocket
┌──────────────────▼──────────────────────┐
│           Controller Layer              │
│  (Nhận request, validate đầu vào,       │
│   trả về ApiResponse chuẩn hóa)         │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│           Service Layer                 │
│  (Xử lý logic nghiệp vụ, gọi repo,      │
│   gọi service ngoài: AWS, OpenAI...)    │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Repository Layer (JPA)          │
│  (Tương tác trực tiếp với MySQL         │
│   thông qua Spring Data JPA)            │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│           MySQL Database                │
└─────────────────────────────────────────┘
```

### Luồng dữ liệu điển hình

```
Request → JwtAuthenticationFilter → Controller → Service → Repository → DB
                                                   │
                                                   ├─→ AWS S3 (upload file)
                                                   ├─→ OpenAI API (moderation)
                                                   └─→ WebSocket Broker (real-time event)
```

### Các module nghiệp vụ

| Module         | Mô tả                                                                     |
|----------------|---------------------------------------------------------------------------|
| `identity`     | Xác thực, đăng ký, đăng nhập, quản lý user & profile                      |
| `feed`         | Bài viết, bình luận, like, kết bạn                                        |
| `chat`         | Nhắn tin 1-1 và nhóm qua WebSocket                                        |
| `notification` | Thông báo real-time khi có sự kiện                                        |
| `moderation`   | Kiểm duyệt nội dung tự động bằng OpenAI                                   |
| `report`       | Báo cáo bài đăng, bình luận, người dùng vi phạm                           |
| `file`         | Upload & quản lý file trên AWS S3                                         |
| `config`       | Cấu hình bảo mật, JWT, WebSocket, CORS, AWS                               |
| `common`       | DTO phản hồi chung (`ApiResponse`)                                        |
| `exception`    | Xử lý ngoại lệ toàn cục                                                   |
| `mapper`       | Ánh xạ Entity ↔ DTO qua MapStruct                                         |

---

## ✨ Các chức năng chính

### 🔐 Xác thực & Phân quyền
- Đăng ký tài khoản, đăng nhập với **JWT Access Token**
- Hỗ trợ chế độ "Nhớ đăng nhập" (token 1 ngày / 14 ngày)
- Blacklist token khi đăng xuất (`InvalidatedToken`)
- Phân quyền **USER** / **ADMIN** qua Spring Security

### 👤 Quản lý hồ sơ người dùng
- Cập nhật thông tin cá nhân: avatar, bio, giới tính, ngày sinh
- Cài đặt quyền riêng tư hồ sơ (`ProfileVisibility`): PUBLIC / FRIEND_ONLY
- Quản lý trạng thái tài khoản (`UserStatus`): ACTIVE / SUSPENDED / DELETED

### 📝 Bài viết (Feed)
- Đăng bài viết kèm ảnh, video (upload lên AWS S3)
- Chỉnh sửa, xóa bài viết
- Phân trang feed; hiển thị bài viết của bạn bè
- Trạng thái bài viết: PENDING (đang kiểm duyệt) / PUBLISHED / REJECTED / UNDER_REVIEW

### 💬 Tương tác bài viết
- **Like** bài viết, bình luận
- **Bình luận**: tạo mới, chỉnh sửa, xóa

### 👥 Kết bạn
- Gửi / chấp nhận / từ chối lời mời kết bạn
- Hủy kết bạn
- Xem danh sách bạn bè & lời mời đang chờ

### 🔎 Tìm kiếm
- Tìm kiếm người dùng
- Tìm kiếm bài đăng

### 🔔 Thông báo (Notifications)
- Nhận thông báo real-time qua **WebSocket** khi:
    - Có lượt like / bình luận bài viết
    - Có lời mời kết bạn mới / được chấp nhận
    - Hệ thống gửi thông báo (system notification)

### 💬 Nhắn tin (Chat)
- Nhắn tin **1-1** (private chat)
- Nhắn tin **nhóm** (group chat): tạo nhóm, đặt tên, thêm/xóa thành viên
- Tin nhắn real-time qua **STOMP / WebSocket**
- Phân trang cursor-based để tải lịch sử tin nhắn
- Hỗ trợ nhiều loại tin nhắn: TEXT, IMAGE, FILE

### 🛡 Kiểm duyệt tự động (Moderation)
- Khi đăng bài/bình luận, hệ thống tạo `ModerationJob`
- `ModerationJobScheduler` định kỳ lấy các job đang chờ rồi gọi **OpenAI Moderation API**
- `ModerationOrchestrator` điều phối quá trình kiểm duyệt
- Nếu vi phạm → bài viết bị REJECTED / UNDER_REVIEW; nếu sạch → chuyển thành PUBLISHED
- Hỗ trợ Spring Retry để tự thử lại khi API ngoài thất bại

### 🧐 Kiểm duyệt thủ công
- Cho phép quản trị viên chỉnh sửa trạng thái bài đăng
- Cho phép quản trị viên kiểm duyệt các bài đăng chưa được AI đánh giá chính xác

### 📁 Quản lý tệp (File)
- Upload ảnh (tối đa 5 MB), video (tối đa 50 MB) lên **AWS S3**
- Tạo **Presigned URL** (hết hạn sau 10 phút) để truy cập file
- Hỗ trợ **Multipart upload** cho file lớn (tối đa 512 MB, URL hết hạn 60 phút)

---

## 📁 Cấu trúc thư mục

```
back-end/
├── src/
│   ├── main/
│   │   ├── java/com/okayji/
│   │   │   ├── OkayjiApplication.java           # Entry point của ứng dụng
│   │   │   │
|   |   |   ├── admin/                           # Module thống kê tổng quan
│   │   │   │   ├── controller/                  
│   │   │   │   ├── dto/response/                
│   │   │   │   └── service/                     
│   │   │   │
│   │   │   ├── identity/                        # Module xác thực & người dùng
│   │   │   │   ├── controller/                  # AuthController, UserController, ProfileController
│   │   │   │   ├── dto/request/                 # RegisterRequest, LoginRequest, UpdateProfileRequest...
│   │   │   │   ├── dto/response/                # AuthResponse, UserResponse, ProfileResponse...
│   │   │   │   ├── entity/                      # User, Profile, Role, InvalidatedToken, UserStatus...
│   │   │   │   ├── repository/                  # UserRepository, ProfileRepository...
│   │   │   │   └── service/                     # AuthService, UserService, ProfileService (+ impl/)
│   │   │   │
│   │   │   ├── feed/                            # Module bài viết & tương tác
│   │   │   │   ├── controller/                  # PostController, CommentController, FeedController, FriendController
│   │   │   │   ├── dto/                         # PostRequest/Response, CommentRequest/Response, FriendReqResponse...
│   │   │   │   ├── entity/                      # Post, Comment, Like, Friend, FriendRequest...
│   │   │   │   ├── repository/                  # PostRepository, CommentRepository, FriendRepository...
│   │   │   │   └── service/                     # PostService, CommentService, FriendService (+ impl/)
│   │   │   │
│   │   │   ├── chat/                            # Module nhắn tin real-time
│   │   │   │   ├── controller/                  # ChatController (REST), ChatWsController (WebSocket)
│   │   │   │   ├── dto/                         # CreateGroupChatRequest, MessageRequest, ChatResponse...
│   │   │   │   ├── entity/                      # Chat, Message, ChatMember, ChatType, MessageType...
│   │   │   │   ├── repository/                  # ChatRepository, MessageRepository, ChatMemberRepository
│   │   │   │   └── service/                     # ChatService, MessageService (+ impl/)
│   │   │   │
│   │   │   ├── notification/                    # Module thông báo real-time
│   │   │   │   ├── controller/                  # NotificationController
│   │   │   │   ├── dto/                         # NotificationResponse, payload types (Like, Comment, Friend...)
│   │   │   │   ├── entity/                      # Notification, NotificationType
│   │   │   │   ├── repository/                  # NotificationRepository
│   │   │   │   └── service/                     # NotificationService, NotificationFactory
│   │   │   │
│   │   │   ├── moderation/                      # Module kiểm duyệt nội dung AI
│   │   │   │   ├── dto/                         # ModerationVerdict
│   │   │   │   ├── entity/                      # ModerationJob, ModerationResult, ModerationJobStatus...
│   │   │   │   ├── repository/                  # ModerationJobRepository, ModerationResultRepository
│   │   │   │   └── service/                     # ModerationService, ModerationOrchestrator, ModerationJobScheduler...
│   │   │   │
│   │   │   ├── file/                            # Module upload file
│   │   │   │   ├── controller/                  # FileController
│   │   │   │   ├── dto/                         # FileUploadRequest, PresignedUrlResponse...
│   │   │   │   └── service/                     # FileService (+ impl/) – tích hợp AWS S3
│   │   │   │
│   │   │   ├── report/                          # Module báo cáo bài đăng vi phạm
│   │   │   │   ├── controller/                  # ReportController
│   │   │   │   ├── dto/request/                 # CreateReportRequest
│   │   │   │   ├── dto/response/                # ReportResponse
│   │   │   │   ├── entity/                      # Report, ReportReason, ReportStatus, ReportTargetType
│   │   │   │   ├── repository/                  # ReportRepository
│   │   │   │   └── service/                     # ReportService (+ impl/), ReportCleanup
│   │   │   │
│   │   │   ├── config/                          # Cấu hình Spring beans
│   │   │   │   ├── SecurityConfig.java          # Cấu hình Spring Security, filter chain
│   │   │   │   ├── JwtAuthenticationFilter.java # Custom filter xác thực JWT mỗi request
│   │   │   │   ├── WebSocketConfig.java         # Cấu hình STOMP endpoint
│   │   │   │   ├── WebSocketSecurityConfig.java # Bảo mật WebSocket channel
│   │   │   │   ├── AwsConfig.java               # Khởi tạo AWS S3 Client
│   │   │   │   ├── WebMvcConfig.java            # Cấu hình argument resolvers
│   │   │   │   └── SchedulingConfiguration.java # Kích hoạt @Scheduled
│   │   │   │
│   │   │   ├── common/
│   │   │   │   └── ApiResponse.java             # Wrapper phản hồi chuẩn hóa cho toàn API
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── AppError.java                # Enum mã lỗi & message
│   │   │   │   ├── AppException.java            # Custom RuntimeException
│   │   │   │   └── GlobalExceptionHandler.java  # @ControllerAdvice xử lý toàn cục
│   │   │   │
│   │   │   └── mapper/                          # MapStruct mapper (Entity ↔ DTO)
│   │   │
│   │   └── resources/
│   │       └── application.yaml                 # Cấu hình ứng dụng (DB, JWT, AWS, AI...)
│   │
│   └── test/
│
├── pom.xml                                      # Khai báo dependency Maven
├── mvnw / mvnw.cmd                              # Maven Wrapper (không cần cài Maven toàn cục)
└── README.md
```

---

## 🚀 Hướng dẫn cài đặt và chạy

### ✅ Yêu cầu môi trường

| Công cụ | Phiên bản tối thiểu |
|---|---|
| Java JDK | 21 |
| Maven | 3.8+ (hoặc dùng `mvnw` đi kèm) |
| MySQL | 9.3+ |
| FFmpeg | Bất kỳ (cần cài và cấu hình đường dẫn) |
| AWS Account | Có S3 bucket tên `okayji`, region `ap-southeast-1` |
| OpenAI API Key | Có quyền truy cập Moderation API |

---

### ⚙️ Cấu hình `application.yaml`

File cấu hình tại `src/main/resources/application.yaml`. Các giá trị nhạy cảm được đọc từ **biến môi trường**:

```yaml
server:
  port: 8686

app:
  ffmpeg:
    path: C:/Program Files/ffmpeg/bin/ffmpeg.exe # based on your device. 
                                                 # If dont have, download at 
                                                 # https://www.ffmpeg.org/download.html
  front-end-domain: "http://localhost:5173,http://localhost:8080"

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/Okayji
    username: ${MYSQL_USERNAME}       # Biến môi trường
    password: ${MYSQL_PASSWORD}       # Biến môi trường
  jpa:
    hibernate:
      ddl-auto: update

  ai:
    openai:
      api-key: ${OPENAI_API_KEY}      # Biến môi trường
      moderation:
        options:
          model: omni-moderation-latest
      base-url: https://api.openai.com

jwt:
  signerKey: ${JWT_SIGNER_KEY}        # Biến môi trường – chuỗi bí mật ký JWT
  accessTokenTime: 1                  # ngày
  accessTokenTimeWithRemember: 14     # ngày

aws:
  region: ap-southeast-1
  s3:
    bucket-name: okayji
  credentials:
    access-key: ${AWS_ACCESS_KEY_ID}  # Biến môi trường
    secret-key: ${AWS_SECRET_ACCESS_KEY}

presigned-url:
  expiration-minutes: 10
multipart-url:
  expiration-minutes: 60

file:
  max-size:
    image: 5       # MB
    video: 50      # MB
    multipart: 512 # MB
    other: 50      # MB
```

#### Thiết lập biến môi trường

```powershell
$env:MYSQL_USERNAME      = "root"
$env:MYSQL_PASSWORD      = "your_password"
$env:JWT_SIGNER_KEY      = "your_secret_key_at_least_32_chars"
$env:OPENAI_API_KEY      = "sk-..."
$env:AWS_ACCESS_KEY_ID   = "AKIA..."
$env:AWS_SECRET_ACCESS_KEY = "..."
```

---

### 🗄 Khởi tạo cơ sở dữ liệu

```sql
CREATE DATABASE Okayji CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> **Lưu ý:** Spring Boot tự động tạo / cập nhật bảng nhờ `ddl-auto: update`. Không cần chạy script SQL thủ công.

---

### 🔨 Build và chạy ứng dụng

#### Sử dụng Maven Wrapper (khuyến nghị – không cần cài Maven)

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

#### Hoặc build JAR rồi chạy

```bash
# Build
./mvnw clean package -DskipTests

# Chạy
java -jar target/okayji-social-media-0.0.1-SNAPSHOT.jar
```

---

### 📖 Tài liệu API (Swagger UI)

Sau khi khởi động, truy cập tài liệu API tại:

```
http://localhost:8686/swagger-ui/index.html
```

---
