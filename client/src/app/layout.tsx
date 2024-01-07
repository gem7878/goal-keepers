'use client';

// import type { Metadata } from "next";
import { Inter } from 'next/font/google';
import './globals.css';
import { Navbar } from '@/components/index.js';
import { usePathname, useRouter } from 'next/navigation';
import { useEffect } from 'react';
import { handleConfirmToken } from './actions';
import { Provider } from 'react-redux';
import { store } from '../redux/store';
import { QueryClient, QueryClientProvider } from 'react-query';

const inter = Inter({ subsets: ['latin'] });

// export const metadata: Metadata = {
//   title: "Create Next App",
//   description: "Generated by create next app",
// };

function RootLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const router = useRouter();
  const loginPath = [
    '/login',
    '/register',
    '/forgot-password',
    '/find',
    'social-register',
    '/redirection',
  ];

  const queryClient = new QueryClient();

  useEffect(() => {
    const fetchData = async () => {
      await handleConfirmToken();
      const tokenData = await handleConfirmToken();
      if (!tokenData && !loginPath.includes(pathname)) {
        router.push('/login');
      }
    };
    fetchData();
  }, [pathname]);

  return (
    <QueryClientProvider client={queryClient}>
      <Provider store={store}>
        <html lang="en">
          <body className={inter.className}>
            <main className="h-[calc(100vh-56px)] w-screen flex flex-col	items-center justify-center">
              {children}
            </main>
            {loginPath.includes(pathname) || <Navbar></Navbar>}
            <div id="portal"></div>
          </body>
        </html>
      </Provider>
    </QueryClientProvider>
  );
}

export default RootLayout;
