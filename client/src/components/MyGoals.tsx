'use client';

import Image from 'next/image';
import React, {
  SetStateAction,
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import Image1 from '../../public/assets/images/goalKeepers.png';
import Image2 from '@/public/assets/images/gem.png';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender } from '@/redux/renderSlice';
import { handleGetGoalListAll } from '@/app/actions';
import { setStateGoal } from '@/redux/renderSlice';

interface myGoalListTypes {
  goalId: number;
  title: string;
  description: string;
  imageUrl: any;
  startDate: string;
  endDate: string;
  shareCnt: number;
  completeDate: string;
  completed: boolean;
  isShare: boolean;
  joinMemberList: string[];
  nickname: string;
}
const MyGoals: React.FC<{
  myGoalList: myGoalListTypes[];
  setSelectGoalNum: React.Dispatch<SetStateAction<number | null>>;
  setMyGoalList: any;
}> = ({ myGoalList, setSelectGoalNum, setMyGoalList }) => {
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [more, setMore] = useState<boolean>(false);
  const containerRef = useRef<any>(null);
  const [goalDoing, setGoalDoing] = useState('doing');

  const dispatch = useDispatch();
  const reduxGoalData = useSelector(selectRender);

  useEffect(() => {
    handleFetchGoalListAll(pageable.pageNumber);
  }, []);
  useEffect(() => {
    if (reduxGoalData.goalBoolean === true) {
      handleFetchGoalListAll(1);
      dispatch(setStateGoal(false));
    }
  }, [reduxGoalData.goalBoolean]);

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
    try {
      const response = await handleGetGoalListAll(form);

      if (response.data) {
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

        setMore(false);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleSelectGoalClick = (index: number) => {
    setSelectGoalNum(index);
  };

  const handleCheckLastPage = () => {
    const pageNumber = pageable.pageNumber + 1;
    if (pageable.last) {
      console.log('마지막 페이지 입니다.');
    } else {
      handleFetchGoalListAll(pageNumber);
    }
  };

  return (
    <div className="w-full h-[calc(100%-40px)] border-x border-b border-orange-300">
      <div>
        <input
          type="radio"
          id="doing"
          name="goal"
          value="doing"
          checked={goalDoing === 'doing'}
          onChange={() => setGoalDoing('doing')}
        ></input>
        <label className="text-black" htmlFor="doing">
          진행 중
        </label>
        <input
          type="radio"
          id="done"
          name="goal"
          value="done"
          checked={goalDoing === 'done'}
          onChange={() => setGoalDoing('done')}
        ></input>
        <label className="text-black" htmlFor="done">
          완료
        </label>
        <label></label>
      </div>
      <ul
        className="w-full max-h-full flex flex-wrap pr-2 pl-4 py-6 overflow-y-scroll gap-2"
        ref={containerRef}
      >
        {myGoalList.map((list, index) => {
          if (
            (goalDoing === 'doing' && !list.completed) ||
            (goalDoing === 'done' && list.completed)
          ) {
            return (
              <li
                key={index}
                className="w-[calc(33%-8px)] aspect-square bg-white relative flex items-center justify-center goal-element"
                onClick={() => handleSelectGoalClick(index)}
              >
                <Image
                  src={list.imageUrl === null ? Image1 : list.imageUrl}
                  alt=""
                  fill
                  style={{
                    objectFit: 'cover',
                    position: 'absolute',
                  }}
                ></Image>
                <div className="w-full h-full bg-black opacity-50 absolute"></div>
                <h3 className="text-white absolute">{list.title}</h3>
              </li>
            );
          }
        })}
      </ul>
    </div>
  );
};
export default MyGoals;
