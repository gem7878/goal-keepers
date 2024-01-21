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
    '/social-register',
    '/redirection',
  ];

  useEffect(() => {
    const fetchData = async () => {
      const tokenData = await handleConfirmToken();
      if (!tokenData && !loginPath.includes(pathname)) {
        router.push('/login');
      }
    };
    fetchData();
  }, [pathname]);

  function setScreenSize() {
    let vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty('--vh', `${vh}px`);
  }

  // const wrapElement: any = document.querySelector('.wrap');
  // wrapElement.style.height = window.innerHeight + 'px';
  useEffect(() => {
    setScreenSize();
  }, []);

  return (
    <Provider store={store}>
      <html lang="en">
        <body className={`${inter.className} h-[calc(var(--vh, 1vh) * 100)]`}>
          <main className="h-[calc(100%-56px)] w-screen flex flex-col	items-center justify-center">
            {children}
          </main>
          {loginPath.includes(pathname) || <Navbar></Navbar>}
          <div id="portal"></div>
        </body>
      </html>
    </Provider>
  );
}

export default RootLayout;
