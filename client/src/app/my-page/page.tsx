"use client";
import React, { useEffect, useMemo, useState } from "react";

const MyPage = () => {
  const [adData, setAdData] = useState([
    {
      title: "광고수신1",
      isAgree: true,
    },
    {
      title: "광고수신2",
      isAgree: true,
    },
    {
      title: "광고수신3",
      isAgree: false,
    },
    {
      title: "광고수신4",
      isAgree: true,
    },
  ]);

  useEffect(() => {}, [adData]);
  const handleClick = ({ index, bool }: { index: number; bool: boolean }) => {
    setAdData((prevAdData) => {
      return prevAdData.map((item, i) => {
        if (i === index) {
          return { ...item, isAgree: bool };
        } else {
          return item;
        }
      });
    });
  };
  return (
    <div className="w-full py-14 px-5 h-full flex flex-col justify-between">
      <section className="border w-full rounded-md h-[17%] flex justify-between py-4 px-8 items-center">
        <div className="flex items-end">
          <h2 className="text-2xl">&quot;밍밍&quot;</h2>
          <label>lv.17</label>
        </div>
        <div className="flex flex-col items-center text-sm">
          <button>닉네임 변경</button>
          <p>레벨 정보</p>
        </div>
      </section>
      <section className="border w-full rounded-md h-[14%] py-4 px-8 flex items-center">
        <h2 className="text-xl">건의함</h2>
      </section>
      <section className="border w-full rounded-md h-[14%] py-4 px-8 flex items-center">
        <h2 className="text-xl">사용정보</h2>
      </section>
      <section className="border w-full rounded-md h-[43%] py-4 px-8 flex flex-col">
        <h2 className="text-xl mb-3">알림 설정</h2>
        <ul className="h-3/4 w-full">
          {adData.map((list, index) => {
            return (
              <li key={index} className="flex justify-between h-1/6 mt-2">
                <label>{list.title}</label>
                {list.isAgree ? (
                  <button
                    className="bg-orange-300 w-1/5 h-full rounded-3xl p-0.5 flex justify-end"
                    onClick={() => handleClick({ index, bool: false })}
                  >
                    <div className="h-full aspect-square rounded-full bg-white right-0"></div>
                  </button>
                ) : (
                  <button
                    className="w-1/5 h-full rounded-3xl bg-gray-300 p-0.5 "
                    onClick={() => handleClick({ index, bool: true })}
                  >
                    <div className="h-full aspect-square rounded-full bg-white"></div>
                  </button>
                )}
              </li>
            );
          })}
        </ul>
      </section>
    </div>
  );
};

export default MyPage;
