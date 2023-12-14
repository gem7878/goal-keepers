"use client";

import { CreateButton, PostBox } from "@/components";
import React, { useRef, useState } from "react";
import Image1 from "../../../public/assets/images/aurora.jpg";
import Image2 from "../../../public/assets/images/gem.png";

const Community = () => {
  const [focusNum, setFocusNum] = useState<number | null>(null);
  const postData = [
    {
      goalTitle: "오로라보기1",
      goalContent:
        "상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용상세내용",
      image: Image1,
      likeCount: 11,
      saveCount: 8,
      postTitle: "포스트1",
      postContent: "포스트 상세내용",
      postDate: "2023-12-01",
      comments: [
        {
          userId: "밍밍밍밍밍",
          content:
            "밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글밍밍의댓글",
        },
      ],
    },
    {
      goalTitle: "오로라보기2",
      goalContent: "상세내용",
      image: Image2,
      likeCount: 11,
      saveCount: 8,
      postTitle: "포스트1",
      postContent: "포스트 상세내용",
      postDate: "2023-12-01",
      comments: [
        {
          userId: "밍밍",
          content: "밍밍의 댓글",
        },
      ],
    },
    {
      goalTitle: "오로라보기3",
      goalContent: "상세내용",
      image: Image1,
      likeCount: 11,
      saveCount: 8,
      postTitle: "포스트1",
      postContent: "포스트 상세내용",
      postDate: "2023-12-01",
      comments: [
        {
          userId: "밍밍",
          content: "밍밍의 댓글",
        },
      ],
    },
    {
      goalTitle: "오로라보기4",
      goalContent: "상세내용",
      image: Image1,
      likeCount: 11,
      saveCount: 8,
      postTitle: "포스트1",
      postContent: "포스트 상세내용",
      postDate: "2023-12-01",
      comments: [
        {
          userId: "밍밍",
          content: "밍밍의 댓글",
        },
      ],
    },
    {
      goalTitle: "오로라보기5",
      goalContent: "상세내용",
      image: Image1,
      likeCount: 11,
      saveCount: 8,
      postTitle: "포스트1",
      postContent: "포스트 상세내용",
      postDate: "2023-12-01",
      comments: [
        {
          userId: "밍밍",
          content: "밍밍의 댓글",
        },
      ],
    },
  ];

  return (
    <div className="w-full	h-full pt-[80px]">
      <header className="w-11/12 inset-x-0 mx-auto flex justify-between	border h-11	fixed top-7 bg-white ">
        <input type="text" className="outline-0	w-4/5 pl-3 z-40"></input>
        <button>search</button>
      </header>
      <section className="z-0 h-full overflow-y-scroll w-full">
        {postData.map((data, index) => {
          return (
            <PostBox
              data={data}
              key={index}
              index={index}
              focusNum={focusNum}
              setFocusNum={setFocusNum}
            ></PostBox>
          );
        })}
      </section>
      <CreateButton></CreateButton>
    </div>
  );
};

export default Community;
