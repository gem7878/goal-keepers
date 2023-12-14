"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import React from "react";

const CreatePostLayout = ({ children }: { children: React.ReactNode }) => {
  const pathname = usePathname();
  console.log(pathname);

  return (
    <section className="gk-primary-create-section">
      <div className="h-4/5 w-full">{children}</div>
      <Link
        className="gk-primary-next-a"
        href={
          pathname === "/create-post/select-goal"
            ? "/create-post/write-post"
            : `/community`
        }
      >
        <button className="w-full h-full">다음</button>
      </Link>
    </section>
  );
};

export default CreatePostLayout;
