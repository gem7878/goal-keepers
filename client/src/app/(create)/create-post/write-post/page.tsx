'use client';

import React, { useEffect, useState } from 'react';

import Image1 from '../../../../../public/assets/images/aurora.jpg';
import Image2 from '../../../../../public/assets/images/gem.png';
import Image from 'next/image';
import { useRouter, useSearchParams } from 'next/navigation';
import { handleGetGoalListAll } from '@/app/actions';
import Link from 'next/link';
import { handleCreatePost } from '../actions';

const WritePost = () => {
  const [goalData, setGoalData] = useState<{
    title: string;
    imageUrl: string | null;
    content: string;
  }>({ title: '', imageUrl: null, content: '' });
  const [editDescription, setEditDescription] = useState('');
  const [postTitle, setPostTitle] = useState('');
  const [postContent, setPostContent] = useState('');

  const searchParams = useSearchParams();
  const router = useRouter();
  const goalId = Number(searchParams.get('goalId'));

  const onFetchGoalListAll = async () => {
    await handleGetGoalListAll()
      .then((response) => {
        const foundGoalData = response.data.find(
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
      title: postTitle,
      content: postContent,
    };
    await handleCreatePost(postData)
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
            src={Image1}
            // src={goalData.imageUrl}
            alt=""
            style={{
              position: 'absolute',
              objectFit: 'cover',
              width: '100%',
              height: '100%',
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
          <input
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
          ></textarea>
        </article>
      </div>
      <Link
        className={
          postTitle.length > 0 && postContent.length > 0
            ? 'gk-primary-next-a'
            : 'gk-primary-next-a-block'
        }
        href={``}
        onClick={() => onCreatePost()}
      >
        <button className="w-full h-full">다음</button>
      </Link>
    </>
  );
};

export default WritePost;
