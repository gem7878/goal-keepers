'use client';

import React, { useEffect, useState } from 'react';

import Image1 from '../../../../../public/assets/images/goalKeepers.png';
import Image2 from '../../../../../public/assets/images/gem.png';
import Image from 'next/image';
import { useRouter, useSearchParams } from 'next/navigation';
import { handleGetGoalListAll } from '@/app/actions';
import Link from 'next/link';
import { handleCreateContent } from '../actions';

const WritePost = () => {
  const [goalData, setGoalData] = useState<{
    title: string;
    imageUrl: string | null;
    content: string;
  }>({ title: '', imageUrl: null, content: '' });
  const [editDescription, setEditDescription] = useState('');
  const [postContent, setPostContent] = useState('');
  const [isPrivate, setIsPrivate] = useState(false)

  const searchParams = useSearchParams();
  const router = useRouter();
  const goalId = Number(searchParams.get('goalId'));
  const pageNum = Number(searchParams.get('pageNum'));

  const onFetchGoalListAll = async () => {
    const form = { pageNum: pageNum };
    await handleGetGoalListAll(form)
      .then((response) => {
        const foundGoalData = response.data.content.find(
          (item: any) => item.goalId === goalId,
        );

        setGoalData(foundGoalData);
        setEditDescription(foundGoalData.description);
      })
      .catch((error) => console.log(error));
  };

  useEffect(() => {
    onFetchGoalListAll();
  }, []);

  const onCreatePost = async () => {
    const postData = {
      goalId: goalId,
      content: postContent,
      privated: isPrivate,
    };
    await handleCreateContent(postData)
      .then((response) => {
        if (response.success) {
          router.push('/community');
        }
      })
      .catch((error) => console.log(error));
  };
  return (
    <>
      <h1 className="gk-primary-h1">포스트를 작성하세요</h1>
      <div className="w-full h-2/3 border rounded-md">
        <header className="w-full h-1/4 relative">
          <Image
            src={goalData.imageUrl === null ? Image1 : goalData.imageUrl}
            alt=""
            fill
            style={{
              position: 'absolute',
              objectFit: 'cover',
            }}
          ></Image>
          <div className="absolute top-0 left-0 w-full h-full bg-black opacity-60	">
            <h2 className="absolute text-white w-full top-1/5 h-1/4 mt-1.5 text-base text-center font-semibold">
              {goalData.title}
            </h2>
            <textarea
              className="absolute bottom-0 m-2 w-[calc(100%-16px)] h-[calc(75%-22px)] bg-inherit border text-white text-xs"
              defaultValue={editDescription}
              onChange={(e) => setEditDescription(e.target.value)}
            ></textarea>
          </div>
        </header>
        <article className="flex-col px-4 py-5 h-3/4">
          {/* <input
            type="text"
            placeholder="포스트 제목을 입력하세요."
            className="text-base w-full border-b h-8"
            value={postTitle}
            onChange={(e) => setPostTitle(e.target.value)}
          ></input>
          <textarea
            placeholder="포스트 내용을 작성하세요."
            className="text-sm w-full border h-[calc(100%-40px)] mt-2 p-2"
            value={postContent}
            onChange={(e) => setPostContent(e.target.value)}
          ></textarea> */}
          <li className="w-full h-9 flex gap-2 items-center">
            <input
              className="w-11/12 text-sm border-b p-1 text-gray-600"
              type="text"
              autoFocus
              placeholder="목표의 현재 진행 상황을 기록하세요!"
              onChange={(e) => setPostContent(e.target.value)}
            ></input>
          </li>

          <button
            onClick={() => {
              // if (addContent) {
              //   onCreatePostContent(data.goalId);
              // } else {
              //   contentRef.current.scrollTop = 0;
              //   setAddContent(true);
              // }
            }}
            className="h-[13%] w-full bg-orange-400 rounded-xl text-sm text-white"
          >
            {/* {addContent ? '입력' : '기록하기'} */}
          </button>
        </article>
      </div>
      <Link
        // className={
        //   postTitle.length > 0 && postContent.length > 0
        //     ? 'gk-primary-next-a'
        //     : 'gk-primary-next-a-block'
        // }
        href={``}
        onClick={() => onCreatePost()}
      >
        <button className="w-full h-full">다음</button>
      </Link>
    </>
  );
};

export default WritePost;
