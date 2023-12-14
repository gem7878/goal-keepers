"use client";

import React, { useState } from "react";
import Image1 from "../../../../public/assets/images/aurora.jpg";
import Image2 from "../../../../public/assets/images/gem.png";
import Image from "next/image";

const SelectGoal = () => {
  const goalList = [
    {
      image: Image1,
      title: "목표제목1",
    },
    {
      image: Image2,
      title: "목표제목2",
    },
    {
      image: Image1,
      title: "목표제목3",
    },
    {
      image: Image2,
      title: "목표제목4",
    },
    {
      image: Image1,
      title: "목표제목5",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
    {
      image: Image1,
      title: "목표제목6",
    },
  ];
  const [hoverNumber, setHoverNumber] = useState<number | null>(null);
  const [selectNumber, setSelectNumber] = useState<number | null>(null);

  const handleMouseEnter = (index: number | null) => {
    setHoverNumber(index);
  };
  const handleImageClick = (index: number | null) => {
    setSelectNumber(index);
  };
  return (
    <>
      <h1 className="gk-primary-h1">나의 목표를 선택하세요</h1>
      <div className="w-full h-4/5 border rounded-md">
        <ul className="w-full max-h-full flex flex-wrap pr-2 pl-4 py-6 overflow-y-scroll gap-2">
          {goalList.map((list, index) => {
            return (
              <li
                key={index}
                onMouseEnter={() => handleMouseEnter(index)}
                onMouseLeave={() => handleMouseEnter(null)}
                onClick={() => handleImageClick(index)}
                className={`relative w-[calc(33%-8px)] aspect-square	${
                  selectNumber === index && "outline outline-4 outline-orange-300"
                }`}
              >
                <Image
                  src={list.image}
                  alt=""
                  style={{ objectFit: "cover", width: "100%", height: "100%" }}
                ></Image>
                {hoverNumber === index ? (
                  <div className="absolute top-0 left-0 w-full h-full bg-black opacity-60	">
                    <h3 className="absolute w-full text-center top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 max-w-full text-sm text-white">
                      {list.title.length > 15
                        ? list.title.slice(0, 15) + "..."
                        : list.title}
                    </h3>
                  </div>
                ) : (
                  <></>
                )}
              </li>
            );
          })}
        </ul>
      </div>
    </>
  );
};

export default SelectGoal;
