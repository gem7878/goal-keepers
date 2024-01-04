"use client";

import React, { useEffect, useState } from "react";
import { kakaoLogin } from "./actions";
import { usePathname, useRouter } from 'next/navigation';

const Redirection = () => {
    const [code, setCode] = useState<string | null>(null);
    const router = useRouter();
        
    useEffect(() => {
        const url = new URL(window.location.href);       
        const code = url.searchParams.get("code");
        setCode(code);
        console.log(code);
    }, []);

    useEffect(() => {
      if (code != null) {
        kakaoLogin(code);
      }
    }, [code]);

    return (
        <div className="h-full w-full flex flex-col items-center justify-center pb-10">
          <h2 className="w-full text-center text-2xl font-extrabold ">
              로그인 중입니다...
          </h2>
        </div>
      );
}

export default Redirection;