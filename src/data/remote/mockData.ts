import { LoginSession, ScaffoldHighlight, ScaffoldSummary } from '../../domain/models';
import { storage } from '../local/storage';

export const DEFAULT_HIGHLIGHTS: ScaffoldHighlight[] = [
  {
    id: "mvi",
    title: "MVI 状态驱动",
    description: "页面状态统一由 StateFlow 驱动，一次性事件走 Effect。",
    stage: "基础能力"
  },
  {
    id: "navigation",
    title: "Navigation Compose",
    description: "默认预留导航骨架，后续可直接扩展为多页面应用。",
    stage: "路由层"
  },
  {
    id: "room",
    title: "Room 本地缓存",
    description: "列表结果可缓存到本地数据库，便于离线读取和后续扩展。",
    stage: "数据层"
  },
  {
    id: "clean-architecture",
    title: "Clean Architecture",
    description: "UI、Domain、Data 按职责分层，减少 feature 间耦合。",
    stage: "工程结构"
  },
  {
    id: "material3",
    title: "Material 3 动态主题",
    description: "内置浅色/深色主题与跟随系统模式，规范化间距与色彩设计系统。",
    stage: "UI 层"
  },
  {
    id: "network",
    title: "统一网络与异常层",
    description: "封装标准请求模型与异常转换映射，保障稳健的错误反馈体验。",
    stage: "网络层"
  }
];

export async function fetchScaffoldSummary(forceRefresh: boolean = false): Promise<ScaffoldSummary> {
  // Simulate network delay
  await new Promise(resolve => setTimeout(resolve, 350));

  const cached = storage.getCachedHighlights();
  if (cached && cached.length > 0 && !forceRefresh) {
    return {
      highlights: cached,
      lastUpdatedAtMillis: storage.getLastUpdated() || Date.now(),
      hasCompletedSetupGuide: storage.getHasCompletedSetup()
    };
  }

  const now = Date.now();
  storage.setCachedHighlights(DEFAULT_HIGHLIGHTS);
  storage.setLastUpdated(now);
  storage.setHasCompletedSetup(true);

  return {
    highlights: DEFAULT_HIGHLIGHTS,
    lastUpdatedAtMillis: now,
    hasCompletedSetupGuide: true
  };
}

export async function apiLogin(email: string, password: string): Promise<LoginSession> {
  await new Promise(resolve => setTimeout(resolve, 500));

  const DEMO_EMAIL = "demo@compose.dev";
  const DEMO_PASSWORD = "password123";

  if (email.toLowerCase() !== DEMO_EMAIL) {
    throw new Error("UNAUTHORIZED");
  }

  if (password !== DEMO_PASSWORD) {
    throw new Error("INVALID_CREDENTIALS");
  }

  const session: LoginSession = {
    userId: "demo-user",
    displayName: "Compose Demo",
    email: email,
    token: "token_demo_compose_template"
  };

  storage.setAuthSession(session);
  return session;
}

export async function apiRegister(displayName: string, email: string, password: string): Promise<LoginSession> {
  await new Promise(resolve => setTimeout(resolve, 600));

  const DEMO_EMAIL = "demo@compose.dev";

  if (!displayName.trim()) {
    throw new Error("NAME_EMPTY");
  }

  if (email.toLowerCase() === DEMO_EMAIL) {
    throw new Error("EMAIL_EXISTS");
  }

  if (password.length < 6) {
    throw new Error("PASSWORD_TOO_SHORT");
  }

  const namePart = email.split('@')[0] || "user";
  const session: LoginSession = {
    userId: `user_${namePart}`,
    displayName: displayName.trim(),
    email: email,
    token: `token_register_${namePart}`
  };

  storage.setAuthSession(session);
  return session;
}
