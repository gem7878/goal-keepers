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
  goalDoing: string;
  setGoalDoing: React.Dispatch<SetStateAction<string>>;
}> = ({
  myGoalList,
  setSelectGoalNum,
  setMyGoalList,
  goalDoing,
  setGoalDoing,
}) => {
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [more, setMore] = useState<boolean>(false);
  const [doneGoalList, setDoneGoalList] = useState<any>();
  const containerRef = useRef<any>(null);

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
  useEffect(() => {}, [myGoalList]);

  useEffect(() => {
    onDoneGoal();
  }, [goalDoing]);

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

  const onDoneGoal = () => {
    if (goalDoing === 'done') {
      // const doneGoalList = myGoalList.filter((item) => item.completed === true);

      const test = [
        {
          goalId: 37,
          nickname: '밍밍밍1026',
          title: 'dfs',
          description: 'cxvxv',
          startDate: '2024-01-15',
          endDate: '2024-01-18',
          imageUrl:
            'https://storage.googleapis.com/goalkeepers-gem.appspot.com/images/827f86d8-08bb-401e-ad44-1d236c9ea366_BackIcon.png?GoogleAccessId=firebase-adminsdk-hl50e@goalkeepers-gem.iam.gserviceaccount.com&Expires=1707020304&Signature=TmzlHzzSkknvD97FKU1GkoPwTV%2B1VYz0QkAl6ICJFeUGN4cTpCh%2FA%2FyJLbQF%2FBo%2Fmosdh%2FcXc5lXj4OlaOcSUThyfyI13R0twk0sAuZRcJYCQ63Fjmlrm9Se7BXVWc49UAQjBVvyfHHsv9VptPzjkiulaTs4LFir2pCNOvYYVEudbQhzhpNIZg0pqhfu9BBdKDettl%2F33Y9ErXxd82z36X8GPRAhSGbGZjdsE03JIeiI%2BKJtEHyQt070H1LJ3lkLFLjalj4OprsZFSNS9SbJOx0KvtEwcR%2BGoE9wEt4%2B5ruLwZ3Gg8b9lUlnhRBzbg%2Fle%2FLCtl2wvuHkAyAkse%2F20Q%3D%3D',
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2024-01-31T08:38:17.353097',
          joinMemberList: [],
        },
        {
          goalId: 36,
          nickname: '밍밍밍1026',
          title: 'title100',
          description: 'description100',
          startDate: '2023-01-08',
          endDate: '2023-08-09',
          imageUrl: null,
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2024-01-31T09:25:42.901274',
          joinMemberList: [],
        },
        {
          goalId: 50,
          nickname: '밍밍밍1026',
          title: 'title100',
          description: 'description100',
          startDate: '2023-01-08',
          endDate: '2023-08-09',
          imageUrl: null,
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2024-01-31T09:25:42.901274',
          joinMemberList: [],
        },
        {
          goalId: 51,
          nickname: '밍밍밍1026',
          title: 'title100',
          description: 'description100',
          startDate: '2023-01-08',
          endDate: '2023-08-09',
          imageUrl: null,
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2024-01-31T09:25:42.901274',
          joinMemberList: [],
        },
        {
          goalId: 52,
          nickname: '밍밍밍1026',
          title: 'title100',
          description: 'description100',
          startDate: '2023-01-08',
          endDate: '2023-08-09',
          imageUrl: null,
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2024-01-31T09:25:42.901274',
          joinMemberList: [],
        },
        {
          goalId: 53,
          nickname: '밍밍밍1026',
          title: 'title100',
          description: 'description100',
          startDate: '2023-01-08',
          endDate: '2023-08-09',
          imageUrl: null,
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2024-01-31T09:25:42.901274',
          joinMemberList: [],
        },
        {
          goalId: 54,
          nickname: '밍밍밍1026',
          title: 'title100',
          description: 'description100',
          startDate: '2023-01-08',
          endDate: '2023-08-09',
          imageUrl: null,
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2024-01-31T09:25:42.901274',
          joinMemberList: [],
        },
        {
          goalId: 40,
          nickname: '밍밍밍1026',
          title: 'title100',
          description: 'description100',
          startDate: '2023-01-08',
          endDate: '2023-08-09',
          imageUrl: null,
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2023-01-31T09:25:42.901274',
          joinMemberList: [],
        },
        {
          goalId: 45,
          nickname: '밍밍밍1026',
          title: 'title100',
          description: 'description100',
          startDate: '2023-01-08',
          endDate: '2023-08-09',
          imageUrl: null,
          shareCnt: 0,
          isShare: false,
          completed: true,
          completeDate: '2025-01-31T09:25:42.901274',
          joinMemberList: [],
        },
      ];

      const groupedByYear: { [year: string]: any[] } = test.reduce(
        (result, goal) => {
          const year = new Date(goal.completeDate).getFullYear().toString();

          if (!result[year]) {
            result[year] = [];
          }

          result[year].push(goal);

          return result;
        },
        {} as { [year: string]: any[] },
      );
      const groupedByYearArray = Object.entries(groupedByYear).map(
        ([year, goals]) => ({
          year,
          goals,
        }),
      );

      setDoneGoalList(groupedByYearArray);
    }
  };

  return (
    <div className="w-full h-[calc(100%-40px)] border-x border-b border-orange-300">
      <div className="flex gap-4 justify-end mt-2 mr-4 h-6">
        <div className="flex items-center">
          <input
            type="radio"
            id="doing"
            name="goal"
            value="doing"
            checked={goalDoing === 'doing'}
            onChange={() => setGoalDoing('doing')}
            className="bg-white border-2 border-orange-300 appearance-none w-3.5 h-3.5	rounded-full checked:bg-orange-300"
          ></input>
          <label
            className="ms-2 text-sm font-medium text-gray-600 dark:text-gray-300"
            htmlFor="doing"
          >
            진행 중
          </label>
        </div>
        <div className="flex items-center">
          <input
            type="radio"
            id="done"
            name="goal"
            value="done"
            checked={goalDoing === 'done'}
            onChange={() => setGoalDoing('done')}
            className="bg-white border-2 border-orange-300 appearance-none w-3.5 h-3.5	rounded-full checked:bg-orange-300"
          ></input>
          <label
            className="ms-2 text-sm font-medium text-gray-600 dark:text-gray-300"
            htmlFor="done"
          >
            완료
          </label>
        </div>
      </div>
      <ul
        className={`w-full  flex pr-2 pl-4 py-5 overflow-y-scroll ${
          goalDoing === 'doing'
            ? 'flex-row flex-wrap gap-2 h-[calc(100%-24px)]'
            : 'flex-col h-full'
        }`}
        ref={containerRef}
      >
        {goalDoing === 'doing'
          ? myGoalList.map((list, index) => {
              if (!list.completed) {
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
            })
          : doneGoalList?.map((data: any, i: number) => {
              return (
                <li key={i} className="text-gray-600 flex flex-col w-full">
                  <h2>{data.year}</h2>
                  <ul className=" flex w-full flex-wrap gap-2">
                    {data.goals.map((list: any, index: number) => {
                      return (
                        <li
                          key={index}
                          className="w-[calc(33%-8px)] aspect-square bg-white relative flex items-center justify-center goal-element"
                          onClick={() => handleSelectGoalClick(index)}
                        >
                          <Image
                            src={
                              list.imageUrl === null ? Image1 : list.imageUrl
                            }
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
                    })}
                  </ul>
                </li>
              );
            })}
      </ul>
    </div>
  );
};
export default MyGoals;
