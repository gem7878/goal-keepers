'use client';
import React, { useEffect, useMemo, useState } from 'react';
import { handleGetUserInfo } from '../actions';
import Link from 'next/link';

const MyPage = () => {
  const [adData, setAdData] = useState([
    {
      title: '광고수신',
      isAgree: true,
    },
    {
      title: '포스트 댓글 알림 수신',
      isAgree: true,
    },
    {
      title: '포스트 좋아요 알림 수신',
      isAgree: false,
    },
    {
      title: '목표 담기 알림 수신',
      isAgree: true,
    },
  ]);
  const [nickname, setNickname] = useState('');
  const [email, setEmail] = useState('');

  useEffect(() => {}, [adData]);
  useEffect(() => {
    onGetUserInfo();
  }, []);

  const onGetUserInfo = async () => {
    await handleGetUserInfo()
      .then((response) => {
        setNickname(response.nickname);
        setEmail(response.emai);
      })
      .catch((error) => console.log(error));
  };

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
          <h2 className="text-2xl">&quot;{nickname}&quot;</h2>
          <label>님</label>
        </div>
        <div className="flex flex-col items-center text-sm">
          <Link
            href={{
              pathname: '/my-page/account',
              query: {
                nickname: nickname,
                email: email,
              },
            }}
          >
            <button>계정 관리</button>
          </Link>
        </div>
      </section>
      <section className="border w-full rounded-md h-[11%] py-4 px-8 flex items-center">
        <h2 className="text-xl">공지사항</h2>
      </section>
      <section className="border w-full rounded-md h-[11%] py-4 px-8 flex items-center">
        <h2 className="text-xl">고객센터</h2>
      </section>
      <section className="border w-full rounded-md h-[40%] py-4 px-8 flex flex-col">
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
      <section className="border w-full rounded-md h-[11%] py-4 px-8 flex items-center justify-between">
        <h2 className="text-xl">버전</h2>
        <label className="text-sm">1.0.0</label>
      </section>
    </div>
  );
};

export default MyPage;
