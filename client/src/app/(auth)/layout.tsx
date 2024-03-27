"use client";

import React from "react";


export default function AuthLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <section className="h-5/6 w-10/12 flex flex-col justify-between">
      {children}
    </section>
  );
}
