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
        <>
          <h2 className="w-full text-center text-3xl font-extrabold mt-20 h-24 font-['MoiraiOne'] text-orange-400">
            {code}
          </h2>
        </>
      );
}

export default Redirection;