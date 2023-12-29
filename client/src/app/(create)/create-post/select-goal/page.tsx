'use client';

import React, { useEffect, useState } from 'react';
import Image1 from '../../../../../public/assets/images/aurora.jpg';
import Image2 from '../../../../../public/assets/images/gem.png';
import Image from 'next/image';
import { handleGetGoalListAll } from '@/app/actions';
import { useDispatch } from 'react-redux';
import { setCreateButton } from '@/redux/renderSlice';
import Link from 'next/link';

interface goalListTypes {
  goalId: number;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  imageUrl: string;
  shareCnt: number;
}

const SelectGoal = () => {
  const [hoverNumber, setHoverNumber] = useState<number | null>(null);
  const [selectNumber, setSelectNumber] = useState<number | null>(null);
  const [myGoalList, setMyGoalList] = useState<goalListTypes[]>([]);
  const [selectGoalId, setSelectGoalId] = useState<number | null>(null);

  const dispatch = useDispatch();

  const handleFetchGoalListAll = async () => {
    await handleGetGoalListAll()
      .then((response) => {
        setMyGoalList(response.data);
      })
      .catch((error) => console.log(error));
  };

  useEffect(() => {
    handleFetchGoalListAll();
  }, []);

  useEffect(() => {
    dispatch(setCreateButton(null));
  }, []);

  const handleMouseEnter = (index: number | null) => {
    setHoverNumber(index);
  };
  const handleImageClick = (index: number | null, goalId: number) => {
    setSelectNumber(index);
    setSelectGoalId(goalId);
  };
  return (
    <>
      <h1 className="gk-primary-h1">나의 목표를 선택하세요</h1>
      <div className="w-full h-2/3 border rounded-md">
        <ul className="w-full max-h-full flex flex-wrap pr-2 pl-4 py-6 overflow-y-scroll gap-2">
          {myGoalList.map((list, index) => {
            return (
              <li
                key={index}
                onMouseEnter={() => handleMouseEnter(index)}
                onMouseLeave={() => handleMouseEnter(null)}
                onClick={() => handleImageClick(index, list.goalId)}
                className={`relative w-[calc(33%-8px)] aspect-square	${
                  selectNumber === index &&
                  'outline outline-4 outline-orange-300'
                }`}
              >
                <Image
                  src={Image1}
                  // src={list.imageUrl}
                  alt=""
                  style={{ objectFit: 'cover', width: '100%', height: '100%' }}
                ></Image>
                {hoverNumber === index ? (
                  <div className="absolute top-0 left-0 w-full h-full bg-black opacity-60	">
                    <h3 className="absolute w-full text-center top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 max-w-full text-sm text-white">
                      {list.title.length > 15
                        ? list.title.slice(0, 15) + '...'
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

      <Link
        className={
          selectNumber !== null
            ? 'gk-primary-next-a'
            : 'gk-primary-next-a-block'
        }
        href={{
          pathname: `/create-post/write-post/`,
          query: { goalId: selectGoalId },
        }}
      >
        <button className="w-full h-full">다음</button>
      </Link>
    </>
  );
};

export default SelectGoal;
