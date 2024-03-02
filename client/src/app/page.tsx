'use client';

import {
  CreateButton,
  GoalModal,
  MyGoals,
  MyPosts,
} from '@/components/index.js';
import { useEffect, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import { StaticImageData } from 'next/image';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender } from '@/redux/renderSlice';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell } from '@fortawesome/free-solid-svg-icons';
import Link from 'next/link';
import { EventSourcePolyfill, NativeEventSource } from 'event-source-polyfill';

import { handleGetToken } from '@/utils/getToken';
import { tokenValue } from './alarm/actions';

interface postContentTypes {
  content: {
    content: string;
    createdAt: string;
    goalDescription: null | string;
    goalId: null | number;
    goalImageUrl: null | string;
    goalTitle: null | string;
    like: boolean;
    likeCnt: number;
    nickname: string;
  };
  goalDescription: string;
  goalId: number;
  goalImageUrl: null | string;
  goalTitle: string;
  goalshareCnt: number;
  postId: number;
  share: boolean;
}

interface myPostListTypes {
  content: postContentTypes[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: {
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    sort: { empty: boolean; sorted: boolean; unsorted: boolean };
    unpaged: boolean;
  };
  size: number;
  sort: { empty: boolean; sorted: boolean; unsorted: boolean };
  totalElements: number;
  totalPages: number;
}

export default function Home() {
  const [isMyGoals, setIsMyGoals] = useState(true);
  const [isOpen, setOpen] = useState(false);
  const [portalElement, setPortalElement] = useState<Element | null>(null);
  const [selectGoalNum, setSelectGoalNum] = useState<number | null>(null);
  const [selectData, setSelectData] = useState<{
    imageUrl: any;
    title: string;
    description: string;
    startDate: string;
    endDate: string;
    shareCnt: number;
    goalId: number;
    completeDate: string | null;
    completed: boolean;
    isShare: boolean;
    joinMemberList: string[];
    nickname: string;
  } | null>(null);
  const [myGoalList, setMyGoalList] = useState<any[]>([]);
  const [myPostList, setMyPostList] = useState<myPostListTypes[]>([]);
  const containerEl = useRef<any>();
  const [goalDoing, setGoalDoing] = useState<string>('doing');
  const [token, setToken] = useState<string | undefined>(undefined);

  useEffect(() => {
    selectGoalNum !== null ? setOpen(true) : setOpen(false);
    if (selectGoalNum !== null) {
      setOpen(true);
      setSelectData(myGoalList[selectGoalNum]);
    } else {
      setOpen(false);
      setSelectData(null);
    }
  }, [selectGoalNum]);

  useEffect(() => {
    setPortalElement(document.getElementById('portal'));
  }, [isOpen]);

  // useEffect(() => {
  //   // date 키값을 string에서 Date로 변환
  //   const updatedList = myGoalList.map((item) => ({
  //     ...item,
  //     completeDate: new Date(item.completeDate),
  //   }));

  //   // 상태 업데이트
  //   setMyGoalList(updatedList);
  // }, []);
  const getToken = async () => {
    const result = await tokenValue();

    console.log(result);

    return result;
    // setToken(result);
  };

  useEffect(() => {
    handleEventSource();
  }, []);

  const handleEventSource = async () => {
    const EventSource = EventSourcePolyfill || NativeEventSource;

    // const token = await tokenValue();
    const token =
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwOTI4NDE3NCwiaWF0IjoxNzA5MTk3Nzc0fQ.SE-L62ZiWO4Ssc2zI9xYFchF-hg9SnbqP5UD6myj4uQ';

    console.log(token);

    const eventSource = new EventSource(
      `${process.env.NEXT_PUBLIC_API_URL}/subscribe`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          Connection: 'keep-alive',
          Accept: 'text/event-stream',
          // 'Content-Type': 'text/event-stream',
          // 'X-Content-Type-Options': 'nosniff',
          // 'X-XSS-Protection': '0',
          // 'Cache-Control': 'no-cache, no-store, max-age=0, must-revalidate',
          // Pragma: 'no-cache',
          // Expires: '0',
          // 'X-Frame-Options': 'DENY',
          // 'Transfer-Encoding': 'chunked',
          // 'Keep-Alive': 'timeout=60',
          // 'Content-Encoding': 'none',
          'Access-Control-Allow-Origin': '*',
        },
        withCredentials: true,
        heartbeatTimeout: 86400000,
      },
    );

    console.log(eventSource);

    // eslint-disable-next-line
    eventSource.onopen = async () => {
      await console.log('sse opened!');
    };

    // eslint-disable-next-line
    eventSource.addEventListener('sse', (event: any) => {
      const data = JSON.parse(event.data);
      const lastEventId = JSON.parse(event.id); // 이걸 저장해둬야 함 Last-Event-ID
      console.log(data, lastEventId);
    });

    // eslint-disable-next-line
    eventSource.onerror = async (e) => {
      await console.log(e);
    };

    return () => {
      eventSource.close();
      // eslint-disable-next-line
    };
  };

  const handleTab = (boolean: boolean) => {
    setIsMyGoals(boolean);
  };

  return (
    <div
      className="flex flex-col	w-full h-full items-center justify-center"
      ref={containerEl}
    >
      <header className="w-full flex flex-col items-end mr-5 mb-6">
        <Link href={'/alarm'}>
          <FontAwesomeIcon icon={faBell} className="w-5 h-5 text-gray-500" />
        </Link>
      </header>
      <section className="w-11/12 h-5/6 text-white">
        <nav className="w-full h-10">
          <ul className="flex h-full">
            <li
              className={`w-1/2 ${
                isMyGoals
                  ? `bg-white border-x border-t border-orange-300 text-orange-500 `
                  : `bg-orange-100 border-b border-orange-300 text-orange-300 `
              } `}
            >
              <button
                className="w-full h-full pr-3"
                onClick={() => handleTab(true)}
              >
                나의 목표
              </button>
            </li>
            <li
              className={`w-1/2 ${
                !isMyGoals
                  ? `bg-white border-x border-t border-orange-300 text-orange-500 `
                  : `bg-orange-100 border-b border-orange-300 text-orange-300 `
              } `}
            >
              <button
                className="w-full h-full pr-3 "
                onClick={() => handleTab(false)}
              >
                나의 포스트
              </button>
            </li>
          </ul>
        </nav>
        {isMyGoals ? (
          <MyGoals
            myGoalList={myGoalList}
            setSelectGoalNum={setSelectGoalNum}
            setMyGoalList={setMyGoalList}
            goalDoing={goalDoing}
            setGoalDoing={setGoalDoing}
          ></MyGoals>
        ) : (
          <MyPosts></MyPosts>
        )}
      </section>
      <CreateButton isMyGoals={isMyGoals}></CreateButton>
      {isOpen && portalElement
        ? createPortal(
            <GoalModal
              setOpen={setOpen}
              selectData={selectData}
              setSelectGoalNum={setSelectGoalNum}
              goalDoing={goalDoing}
            />,
            portalElement,
          )
        : null}
    </div>
  );
}
