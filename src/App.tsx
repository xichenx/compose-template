import React, { useState } from 'react';
import { AppRoute } from './domain/models';
import { ThemeProvider } from './ui/theme/ThemeContext';
import { SplashScreen } from './ui/screens/SplashScreen';
import { AuthEntryScreen } from './ui/screens/AuthEntryScreen';
import { LoginScreen } from './ui/screens/LoginScreen';
import { RegisterScreen } from './ui/screens/RegisterScreen';
import { HomeScreen } from './ui/screens/HomeScreen';
import { Snackbar } from './ui/components/Snackbar';

export const AppContent: React.FC = () => {
  const [currentRoute, setCurrentRoute] = useState<AppRoute>('splash');
  const [snackbarMessage, setSnackbarMessage] = useState<string | null>(null);

  const showSnackbar = (msg: string) => {
    setSnackbarMessage(msg);
  };

  return (
    <div className="relative min-h-screen">
      {currentRoute === 'splash' && (
        <SplashScreen
          onNavigateToAuth={() => setCurrentRoute('auth_entry')}
          onNavigateToHome={() => setCurrentRoute('home')}
        />
      )}

      {currentRoute === 'auth_entry' && (
        <AuthEntryScreen
          onNavigateToLogin={() => setCurrentRoute('login')}
          onNavigateToRegister={() => setCurrentRoute('register')}
          onNavigateToHome={() => setCurrentRoute('home')}
        />
      )}

      {currentRoute === 'login' && (
        <LoginScreen
          onNavigateBack={() => setCurrentRoute('auth_entry')}
          onNavigateToRegister={() => setCurrentRoute('register')}
          onNavigateToHome={() => setCurrentRoute('home')}
          onShowMessage={showSnackbar}
        />
      )}

      {currentRoute === 'register' && (
        <RegisterScreen
          onNavigateBack={() => setCurrentRoute('auth_entry')}
          onNavigateToHome={() => setCurrentRoute('home')}
          onShowMessage={showSnackbar}
        />
      )}

      {currentRoute === 'home' && (
        <HomeScreen
          onNavigateToAuth={() => setCurrentRoute('auth_entry')}
          onShowMessage={showSnackbar}
        />
      )}

      <Snackbar
        message={snackbarMessage}
        onDismiss={() => setSnackbarMessage(null)}
      />
    </div>
  );
};

export default function App() {
  return (
    <ThemeProvider>
      <AppContent />
    </ThemeProvider>
  );
}
