'use client';
import {
  CreateButton,
  CommunityBox,
  CommunityBoxDetail,
} from '@/components/index.js';
import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import { handleGetCommunityAll } from './actions';
import { handleGetUserInfo } from '../actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStateCommunity } from '@/redux/renderSlice';
import {
  handleCreateShare,
  handleDeleteShare,
  handleGetShare,
} from './share/actions';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faSearch,
  faChevronCircleRight,
  faChevronCircleLeft,
} from '@fortawesome/free-solid-svg-icons';

interface communityContentList {
  nickname: string;
  content: number;
  updatedAt: string;
  likeCnt: number;
  goalId: number;
  goalTitle: string;
  goalDescription: string;
  goalImageUrl: null | string;
  like: false;
}
interface communityContentTypes {
  originalGoalId: number;
  originalGoalTitle: string;
  originalGoalDescription: string;
  originalGoalImageUrl: null | string;
  originalGoalshareCnt: number;
  joinMemberList: string[];
  contentList: communityContentList[];
  count: null | number;
  share: boolean;
}

interface myCommunityListTypes {
  timestamp: string;
  success: boolean;
  message: null | string;
  data: {
    content: communityContentTypes[];
  };
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  first: boolean;
  numberOfElements: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  empty: boolean;
}
const Community = (props: any) => {
  const [focusNum, setFocusNum] = useState<number | null>(null);
  const [communityData, setCommunityData] = useState<communityContentTypes[]>(
    [],
  );
  const [nickname, setNickname] = useState('');
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [page, setPage] = useState(pageable.pageNumber);
  const [sort, setSort] = useState('NEW');

  const dispatch = useDispatch();
  const reduxCommunityData = useSelector(selectRender);

  useEffect(() => {
    onGetUserInfo();
  }, []);

  useEffect(() => {
    onGetCommunityAll(page);
  }, [page, reduxCommunityData.communityBoolean]);

  const onGetUserInfo = async () => {
    await handleGetUserInfo()
      .then((response) => {
        setNickname(response.nickname);
      })
      .catch((error) => console.log(error));
  };

  const onGetCommunityAll = async (pageParam: number) => {
    const formData = {
      pageNum: pageParam,
      sort: sort,
    };
    const response = await handleGetCommunityAll(formData);
    console.log(response);

    if (response.success) {
      setCommunityData(response.data.content);
      setPageable({
        pageNumber: response.data.pageable.pageNumber + 1,
        last: response.data.last,
      });
    }
  };

  return (
    <div className="w-full h-full pt-[40px] flex flex-col">
      <header className="w-11/12 inset-x-0 mx-auto flex justify-between	border h-11 top-7 bg-white rounded-full items-center">
        <input type="text" className="outline-0	w-4/5 pl-3 z-40"></input>
        <FontAwesomeIcon
          icon={faSearch}
          className="w-6 h-6 mr-3 text-gray-500"
        />
      </header>
      <section className="flex gap-4 justify-end mt-2 mr-4 mb-2 h-6">
        <div className="flex items-center">
          <input
            type="radio"
            id="new"
            name="community"
            value="new"
            checked={sort === 'NEW'}
            onChange={() => setSort('NEW')}
            className="bg-white border-2 border-orange-300 appearance-none w-3.5 h-3.5	rounded-full checked:bg-orange-300"
          ></input>
          <label
            className="ms-2 text-sm font-medium text-gray-600 dark:text-gray-300"
            htmlFor="new"
          >
            최신순
          </label>
        </div>
        <div className="flex items-center">
          <input
            type="radio"
            id="popular"
            name="community"
            value="popular"
            checked={sort === 'POPULAR'}
            onChange={() => setSort('POPULAR')}
            className="bg-white border-2 border-orange-300 appearance-none w-3.5 h-3.5	rounded-full checked:bg-orange-300"
          ></input>
          <label
            className="ms-2 text-sm font-medium text-gray-600 dark:text-gray-300"
            htmlFor="popular"
          >
            인기순
          </label>
        </div>
      </section>
      <section className="z-0 h-full overflow-y-scroll w-full py-4">
        {communityData.map((data, index) => {
          return focusNum === index ? (
            <CommunityBoxDetail data={data}></CommunityBoxDetail>
          ) : (
            <CommunityBox
              data={data}
              key={index}
              index={index}
              focusNum={focusNum}
              setFocusNum={setFocusNum}
            ></CommunityBox>
          );
        })}
        {/* <section className="h-[30px] w-full flex justify-center gap-4 text-gray-600 ">
          <FontAwesomeIcon
            icon={faChevronCircleLeft}
            className="cursor-pointer"
            onClick={() => {
              if (page > 1) {
                setPage(page - 1);
                setFocusNum(null);
              } else {
                alert('첫 번째 페이지 입니다.');
              }
            }}
          />
          <FontAwesomeIcon
            icon={faChevronCircleRight}
            className="cursor-pointer"
            onClick={() => {
              if (pageable.last) {
                alert('마지막 페이지 입니다.');
              } else {
                setPage(page + 1);
                setFocusNum(null);
              }
            }}
          />
        </section> */}
      </section>
    </div>
  );
};
export default Community;
