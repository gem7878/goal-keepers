import React from "react";

import Image1 from "../../../../public/assets/images/aurora.jpg";
import Image2 from "../../../../public/assets/images/gem.png";
import Image from "next/image";
const WritePost = () => {
  const goalData = {
    title: "오로라보기",
    image: "../../../../public/assets/images/aurora.jpg",
    detail: "오로라보기상세내용",
  };

  return (
    <>
      <h1 className="gk-primary-h1">포스트를 작성하세요</h1>
      <div className="w-full h-4/5 border rounded-md">
        <header className="w-full h-1/4 relative">
          <Image
            src={Image1}
            alt=""
            style={{
              position: "absolute",
              objectFit: "cover",
              width: "100%",
              height: "100%",
            }}
          ></Image>
          <div className="absolute top-0 left-0 w-full h-full bg-black opacity-60	">
            <h2 className="absolute text-white w-full top-1/5 h-1/4 mt-1.5 text-base text-center font-semibold">
              {goalData.title}
            </h2>
            <textarea className="absolute bottom-0 m-2 w-[calc(100%-16px)] h-[calc(75%-22px)] bg-inherit border text-white text-xs">
              {goalData.detail}
            </textarea>
          </div>
        </header>
        <article className="flex-col px-4 py-5 h-3/4">
          <input
            type="text"
            placeholder="포스트 제목을 입력하세요."
            className="text-base w-full border-b h-8"
          ></input>
          <textarea
            placeholder="포스트 내용을 작성하세요."
            className="text-sm w-full border h-[calc(100%-40px)] mt-2 p-2"
          ></textarea>
        </article>
      </div>
    </>
  );
};

export default WritePost;
