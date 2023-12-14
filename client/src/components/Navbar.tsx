import Link from "next/link";
import React from "react";

const Navbar = () => {
  const listName = "border border-black text-xs w-10 h-10 text-center";
  return (
    <nav className="w-[100vw]  border border-black fixed h-14 px-4 bg-white">
      <ul className=" w-full h-full flex bottom-0  justify-around items-center">
        <li className={listName}>
          <Link href={"/"}>메인</Link>
        </li>
        <li className={listName}>
          <Link href={"/community"}>
            커뮤<br></br>니티
          </Link>
        </li>
        <li className={listName}>
          <Link href={"/goal-list"}>
            골<br></br>리스트
          </Link>
        </li>
        <li className={listName}>
          <Link href={"/game"}>게임</Link>
        </li>
        <li className={listName}>
          <Link href={"/my-page"}>
            마이<br></br>페이지
          </Link>
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;
