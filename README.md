# Compose Template (React Rewrite)

A complete React rewrite of the **Compose MVI Scaffold** project originally written in Android Jetpack Compose. This project preserves the core features, architectural boundaries, responsive navigation, bilingual localization, and user flow from the original application.

## Key Features

- **Responsive Multi-Form Navigation**:
  - **Mobile / Compact**: Material 3 floating pill bottom navigation bar with active tab indicator.
  - **Tablet & Desktop**: Adaptive persistent sidebar / navigation rail.
- **Three-Tier Home Views**:
  - **Overview**: Module introduction, setup status indicator, last synced timestamp, quick actions, and top highlight cards.
  - **Modules**: Module search, filter chips by architectural stage (`UI 层`, `路由层`, `基础能力`, `数据层`, `工程结构`, `网络层`), list items with click feedback, and counter.
  - **Profile**: Active user session details, login / logout action, dynamic language switcher, dynamic theme switcher, and integration status notes.
- **Authentication Flows**:
  - **Auth Entry**: Split-card layout for choosing Login or Registration, with responsive layout.
  - **Login**: Email and password validation, show/hide password visibility toggle, built-in demo credentials autofill (`demo@compose.dev` / `password123`), error handling, and success confirmation modal.
  - **Register**: Nickname, email uniqueness check, password length check, confirm password matching, and automatic sign-in upon registration.
  - **Splash Screen**: Session verification with smooth loading state and automatic redirection.
- **Theme & Localization**:
  - Full bilingual support for **English** and **Simplified Chinese (简体中文)** matching all original strings.
  - **Material 3 Light / Dark / Follow System** theming matching the original Android design tokens (`#3D5AFE` brand primary, container tints, dark background `#020617`, etc.).
- **Client-Side Persistence**:
  - Preferences and authenticated sessions persist seamlessly in `localStorage`.

## Tech Stack

- **React 18** with **TypeScript**
- **Vite** build tooling
- **Tailwind CSS v4**
- **Lucide Icons**