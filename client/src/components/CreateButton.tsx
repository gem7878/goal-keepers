"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import React from "react";

const CreateButton = () => {
  const pathname = usePathname();

  return (
    <Link href={pathname === "/" ? "/create-goal/1" : "/create-post/select-goal"}>
      <button className="fixed bottom-20 w-16 h-16 right-2 border border-black rounded-full	bg-white	">
        Create {pathname === "/" ? "goal" : "post"}
      </button>
    </Link>
  );
};

export default CreateButton;
