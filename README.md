src/
├── domain/               # Chứa logic nghiệp vụ thuần (không phụ thuộc framework)
│   ├── model/            # Các entity (đối tượng nghiệp vụ)
│   ├── repository/       # Interface cho data access (abstract)
│   └── service/          # Các use case (ứng dụng business logic)
│
├── application/          # Thực thi các use case, đôi khi được gộp vào domain
│   └── usecase/          # Gọi các service, phối hợp nghiệp vụ
│
├── infrastructure/       # Triển khai các interface (có thể dùng framework/lib)
│   ├── persistence/      # Repository thực tế (VD: dùng JPA, JDBC, Mongo, ...)
│   ├── config/           # File config, bean setup
│   └── external/         # Giao tiếp với hệ thống bên ngoài (email, API, ...)
│
├── interface/            # Giao tiếp với user hoặc hệ thống khác
│   ├── controller/       # REST API / Web Controller
│   └── dto/              # Request / Response model
│
└── main/                 # Entry point (SpringBootApplication class)
