"use client";

import Link from "next/link";
import React from "react";
import { usePathname } from "next/navigation";

const navLink = [
  { name: "Register", href: "/register" },
  { name: "Login", href: "/login" },
  { name: "Forgot Password", href: "/forgot-password" },
];

export default function AuthLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const pathname = usePathname();
  return (
    <section className="h-5/6 w-10/12 flex flex-col justify-between">
      {/* {navLink.map((link) => {
        const isActive = pathname.startsWith(link.href);
        return (
          <Link
            href={link.href}
            key={link.name}
            className={isActive ? "font-bold mr-4" : "text-blue-500"}
          >
            {link.name}
          </Link>
        );
      })} */}
      {children}
    </section>
  );
}
