'use client';

import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import Image1 from '../../../../../public/assets/images/goalKeepers.png';
import Image2 from '../../../../../public/assets/images/gem.png';
import Image from 'next/image';
import { useDispatch } from 'react-redux';
import { setCreateButton } from '@/redux/renderSlice';
import Link from 'next/link';
import { handleSelectMyPost } from '../actions';

interface goalListTypes {
  goalId: number;
  title: string;
  description: string;
  privated: boolean;
  imageUrl: string;
}

const SelectGoal = () => {
  const [hoverNumber, setHoverNumber] = useState<number | null>(null);
  const [selectNumber, setSelectNumber] = useState<number | null>(null);
  const [myGoalList, setMyGoalList] = useState<goalListTypes[]>([]);
  const [selectGoalId, setSelectGoalId] = useState<number | null>(null);
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [more, setMore] = useState<boolean>(false);
  const containerRef = useRef<any>(null);
  const [pageNum, setPageNum] = useState(1);

  const dispatch = useDispatch();

  useEffect(() => {
    handleFetchGoalListAll(pageable.pageNumber);
  }, []);
  useEffect(() => {
    if (more) {
      handleCheckLastPage();
    }
  }, [more]);

  useLayoutEffect(() => {
    if (containerRef.current) {
      containerRef.current.addEventListener('scroll', handleScroll);
      return () =>
        containerRef.current.removeEventListener('scroll', handleScroll);
    }
  }, []);

  const handleScroll = useCallback(() => {
    if (containerRef.current) {
      const elements = containerRef.current.querySelectorAll('.goal-element');

      if (elements.length > 0) {
        const lastElement = elements[elements.length - 1];

        const lastElementBottom = lastElement.getBoundingClientRect().bottom;
        const parentElementBottom =
          lastElement.parentElement.getBoundingClientRect().bottom;

        if (lastElementBottom - parentElementBottom < -20) {
          setMore(true);
        }
      }
    }
  }, []);

  const handleFetchGoalListAll = async (pageParam: number) => {
    const form = { pageNum: pageParam };
    await handleSelectMyPost(form)
      .then((response) => {
        if (more) {
          setMyGoalList((prevPostData: any) => [
            ...prevPostData,
            ...response.data.content,
          ]);
          setPageable({
            pageNumber: response.data.pageable.pageNumber + 1,
            last: response.data.last,
          });
        } else {
          setMyGoalList(response.data.content);
          setPageable({
            pageNumber: response.data.pageable.pageNumber + 1,
            last: response.data.last,
          });
        }
        setPageNum(response.data.pageable.pageNumber + 1);
        setMore(false);
      })
      .catch((error) => console.log(error));
  };

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

  const handleCheckLastPage = () => {
    const pageNumber = pageable.pageNumber + 1;
    if (pageable.last) {
      console.log('마지막 페이지 입니다.');
    } else {
      handleFetchGoalListAll(pageNumber);
    }
  };

  console.log(myGoalList);
  
  return (
    <>
      <h1 className="gk-primary-h1">나의 목표를 선택하세요</h1>
      <div className="w-full h-2/3 border rounded-md">
        <ul
          className="w-full max-h-full flex flex-wrap pr-2 pl-4 py-6 overflow-y-scroll gap-2"
          ref={containerRef}
        >
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
                } goal-element`}
              >
                <Image
                  src={list.imageUrl === null ? Image1 : list.imageUrl}
                  fill
                  alt=""
                  style={{ objectFit: 'cover' }}
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
          query: { goalId: selectGoalId, pageNum: pageNum },
        }}
      >
        <button className="w-full h-full">다음</button>
      </Link>
    </>
  );
};

export default SelectGoal;
