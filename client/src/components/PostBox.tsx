"use client";

import Image from "next/image";
import React, { useRef, useState } from "react";

interface postDataTypes {
  goalTitle: string;
  goalContent: string;
  image: any;
  likeCount: number;
  saveCount: number;
  postTitle: string;
  postContent: string;
  postDate: string;
  comments: {
    userId: string;
    content: string;
  }[];
}

const PostBox: React.FC<{
  data: postDataTypes;
  index: number;
  focusNum: number | null;
  setFocusNum: React.Dispatch<React.SetStateAction<number | null>>;
}> = ({ data, index, focusNum, setFocusNum }) => {
  const likeRef = useRef<HTMLUListElement>(null);

  const handleFocus = (e: { target: any }) => {
    if (!likeRef.current?.contains(e.target)) {
      if (focusNum === index) {
        // setFocusNum(null);
      } else {
        setFocusNum(index);
      }
    }
  };
  return focusNum !== index ? (
    <article
      onClick={(e) => handleFocus(e)}
      className="h-44
      flex
      justify-between
      p-3
      mb-4
      border
      rounded-md
      duration-100
      w-11/12
      inset-x-0
      mx-auto
      "
    >
      <div className="w-1/2 h-full relative px-2 z-0 flex flex-col	justify-between items-center">
        <Image
          src={data.image}
          alt=""
          style={{
            width: "100%",
            height: "75%",
            objectFit: "cover",
            zIndex: 1,
          }}
        ></Image>
        <h3 className="text-center px-1 max-w-fit mx-4	text-white	font-bold absolute top-1/3 -translate-y-1/3 z-10 bg-black text-ellipsis	">
          {data.goalTitle}
          {data.goalTitle.length > 22 && "..."}
        </h3>
        <ul ref={likeRef} className="flex w-full justify-center	gap-2">
          <li className="flex items-center">
            <button>🧡</button>
            <label className="text-xs	">{data.likeCount}</label>
          </li>
          <li className="flex items-center">
            <button>➕</button>
            <label className="text-xs	">{data.saveCount}</label>
          </li>
        </ul>
      </div>
      <div className="w-1/2	flex flex-col justify-between">
        <div className="mt-2">
          <h3 className="font-bold	">{data.postTitle}</h3>
          <p className="text-sm	">{data.postContent}</p>
        </div>
        <label className="text-xs	w-full text-right	">{data.postDate}</label>
      </div>
    </article>
  ) : (
    <article
      onClick={(e) => handleFocus(e)}
      className="h-3/4 flex-col p-3 mb-4 border rounded-md duration-100	
      w-11/12
      inset-x-0
      mx-auto"
    >
      <div className="w-full h-1/4 relative z-0 flex rounded-md	">
        <Image
          src={data.image}
          alt=""
          style={{
            width: "100%",
            height: "100%",
            objectFit: "cover",
            // zIndex: 1,
            position: "absolute",
            borderRadius: "5px",
          }}
        ></Image>
        <div className="w-full h-full bg-black absolute opacity-50"></div>
        <h3 className="text-center px-1  mx-4	text-white	font-bold absolute top-1/4 -translate-y-1/3 z-10 text-ellipsis	">
          {data.goalTitle.length > 18
            ? data.goalTitle.slice(0, 18) + "..."
            : data.goalTitle}
        </h3>
        <p className="text-white w-5/6 absolute top-1/3 text-xs mt-2 mx-4">
          {data.goalContent.length > 47
            ? data.goalContent.slice(0, 47) + "..."
            : data.goalContent}
        </p>
        <ul
          ref={likeRef}
          className="absolute right-0 bottom-0 mb-1 mr-3 flex justify-center	text-white gap-2"
        >
          <li className="flex items-center">
            <button>🧡</button>
            <label className="text-xs	">{data.likeCount}</label>
          </li>
          <li className="flex items-center">
            <button>➕</button>
            <label className="text-xs	">{data.saveCount}</label>
          </li>
        </ul>
      </div>
      <div className="w-full h-[40%]	pt-2 flex flex-col">
        <h3 className="font-bold">
          {data.postTitle.length > 20
            ? data.postTitle.slice(0, 20) + "..."
            : data.postTitle}
        </h3>
        <h5 className="text-xs	w-full text-right	">{data.postDate}</h5>
        <p className="text-sm	">{data.postContent}</p>
      </div>
      <div className="w-full h-[35%] flex-col text-sm">
        <h3 className="h-4">댓글</h3>
        <div className="w-full h-[calc(100%-20px)] mt-1 border rounded-lg p-2">
          <ul className="w-full h-3/4 overflow-y-scroll">
            {data.comments.map((list, index) => {
              return (
                <li key={index} className="flex-col w-full">
                  <h4 className="text-xs w-full font-bold">{list.userId}</h4>
                  <p className="text-xs w-auto">{list.content}</p>
                </li>
              );
            })}
          </ul>
          <div className="w-full flex justify-between h-1/4">
            <input
              type="text"
              className="border rounded-lg w-5/6 pl-2 text-xs"
              placeholder="댓글을 입력하세요."
            />
            <button className="w-[13%] bg-orange-300 rounded-lg text-xs text-white border border-white">
              확인
            </button>
          </div>
        </div>
      </div>
    </article>
  );
};

export default PostBox;
