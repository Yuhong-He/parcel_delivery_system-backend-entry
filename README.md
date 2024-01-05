# UCD Parcel Delivery System - Backend
This project is a team project for assignment 2 of COMP41720 Distributed Systems module in UCD master's programme.

- GitHub Repo:
  - Frontend: [Yuhong-He/ucd_parcel_frontend](https://github.com/Yuhong-He/ucd_parcel_frontend)
  - Backend: [Yuhong-He/ucd_parcel_backend](https://github.com/Yuhong-He/ucd_parcel_backend)

- Access via:
  - Frontend: [www.ucdparcel.ie](https://www.ucdparcel.ie)
  - Backend (Entry System): [api.ucdparcel.ie](https://api.ucdparcel.ie)
  - Backend (Entry System Swagger): [api.ucdparcel.ie/swagger-ui/index.html](https://api.ucdparcel.ie/swagger-ui/index.html)
  - Jenkins: [162.62.224.31:8080](http://162.62.224.31:8080/)

## Team
- Team: Group - 13
- Team members: 
  - Yuhong He 19326053 ([Yuhong-He](https://github.com/Yuhong-He))
  - Jie Wang 23205138 ([Jay-Wang2000](https://github.com/Jay-Wang2000))
  - Yunni Xiao 21212287 ([SnackPocket](https://github.com/SnackPocket))
  - Yu Bai 22212818 ([YuBaiBY](https://github.com/YuBaiBY))

## System Overview
This project contains 8 subsystems, for more details, please refer to the Final Team Project Report.
1. [Entry](https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Entry)
2. [Email](https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Email)
3. [Estate](https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Estate)
4. [Broker](https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Broker)
5. [Postman](https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Postman)
6. [Merville](https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Merville)
7. [Receiver](https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Receiver)
8. [Database](https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Database)

## Project Introduction
This application is a UCD parcel delivery system, aiming to build an informative delivery system for UCD staff. This application is a frontend and backend separated system and mocks the real logistics system at UCD. According to the relevant research, the UCD post system is a central-dispense endpoint in the logistics system. This system contains logistics modules for each apartment including “Estate Service”, “Broker”, “Postman”, “Merville” and “Receiver”, as well as other modules including “Email”, “Database” and “Register”, which aims to assist the logistics ones’ process.

This application provides full process track as soon as the parcel arrives and mitigates the possibility of the loss of the package. Each role in the system has a clear view of the relevant information. The application provides strong scalability, maintainability, and fault tolerance by combining various distributed system technologies.

The application is abstracted as 3 layers, the application layer, the backend and frontend layer, and the Delivery system layer. System administrators can operate the application layer. Frontend and backend layer interacts with each other. At last, the delivery layer residents within the backend layer and is unseeable to the frontend layer. We used various distributed system techniques to implement the system.

## Final Team Project Report

## Demo Video
[![YouTube](https://i9.ytimg.com/vi/H3viuY6ikGU/mqdefault.jpg?v=6598846b&sqp=CJSI4qwG&rs=AOn4CLAtMzZOq-Ia3kPXjfP5HMEl0VZ_gg)](https://youtu.be/H3viuY6ikGU)