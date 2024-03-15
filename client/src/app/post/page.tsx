'use client';
import {
  CreateButton,
  PostBox,
  PostBoxDetail,
  SearchBox,
} from '@/components/index.js';
import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import { handleGetPostAll, handleCheerPost } from './actions';
import { handleGetUserInfo } from '../actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStatePost } from '@/redux/renderSlice';
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

interface postContentTypes {
  content: string;
  createdAt: string;
  like: boolean;
  likeCnt: number;
  nickname: string;
  contentId: number;
}

interface postTypes {
  content: postContentTypes;
  goalDescription: string;
  goalId: number;
  goalImageUrl: null | string;
  goalTitle: string;
  goalshareCnt: number;
  postId: number;
  share: boolean;
  cheer: boolean;
  myPost: false;
  nickname: string;
  postCheerCnt: number;
  privated: boolean;
}

const Post = () => {
  const [focusNum, setFocusNum] = useState<number | null>(null);
  const [postData, setPostData] = useState<postTypes[]>([]);
  const [nickname, setNickname] = useState('');
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [page, setPage] = useState(pageable.pageNumber);
  const [sort, setSort] = useState('NEW');

  const dispatch = useDispatch();
  const reduxPostData = useSelector(selectRender);

  useEffect(() => {
    onGetUserInfo();
  }, []);

  useEffect(() => {
    onGetAllPost(page);
  }, [page, reduxPostData.postBoolean, sort, reduxPostData.privateBoolean]);

  const onGetAllPost = async (pageParam: number) => {
    const formData = {
      pageNum: pageParam,
      sort: sort,
    };
    await handleGetPostAll(formData)
      .then((response) => {
        setPostData(response.content);
        setPageable({
          pageNumber: response.pageable.pageNumber + 1,
          last: response.last,
        });
      })
      .catch((error) => console.log(error));
  };

  const onGetUserInfo = async () => {
    await handleGetUserInfo()
      .then((response) => {
        setNickname(response.nickname);
      })
      .catch((error) => console.log(error));
  };

  const onCheerPost = async (index: number) => {
    await handleCheerPost(postData[index].postId)
      .then((response) => {
        if (response.success) {
          dispatch(setStatePost(!reduxPostData.postBoolean));
        }
      })
      .catch((error) => console.log(error));
  };
  const onShareGoal = async (index: number) => {
    await handleCreateShare(postData[index].goalId)
      .then((response) => {
        if (response.success) {
          dispatch(setStatePost(!reduxPostData.postBoolean));
        }
      })
      .catch((error) => console.log(error));
  };
  const onGetShareData = async (goalId: number) => {
    await handleGetShare(goalId)
      .then(async (response) => {
        if (response.success) {
          await onDeleteShareGoal(response.data.goalId);
        }
      })
      .catch((error) => console.log(error));
  };
  const onDeleteShareGoal = async (goalId: number) => {
    await handleDeleteShare(goalId)
      .then((response) => {
        if (response.success) {
          dispatch(setStatePost(!reduxPostData.postBoolean));
        }
      })
      .catch((error) => console.log(error));
  };

  const onChangeSort = async (state: string) => {
    setSort(state);
    setPageable({
      pageNumber: 1,
      last: false,
    });
  };

  return (
    <div className="w-full h-full pt-[40px] flex flex-col">
      <SearchBox
        pageNumber={pageable.pageNumber}
        sort={sort}
        onChangeSort={onChangeSort}
        setData={setPostData}
        setPageable={setPageable}
      ></SearchBox>
      <section className="z-0 h-full overflow-y-scroll w-full py-4">
        {postData.map((data, index) => {
          if (focusNum === index) {
            return (
              <PostBoxDetail
                data={data}
                key={index}
                myNickname={nickname}
                setFocusNum={setFocusNum}
                index={index}
                onCheerPost={onCheerPost}
                onShareGoal={onShareGoal}
                onGetShareData={onGetShareData}
              ></PostBoxDetail>
            );
          } else {
            return (
              <PostBox
                data={data}
                key={index}
                index={index}
                focusNum={focusNum}
                setFocusNum={setFocusNum}
                onCheerPost={onCheerPost}
                onShareGoal={onShareGoal}
                onGetShareData={onGetShareData}
              ></PostBox>
            );
          }
        })}

        <section className="h-[30px] w-full flex justify-center gap-4 text-gray-600 ">
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
        </section>
      </section>
      <CreateButton isMyGoals={false}></CreateButton>
    </div>
  );
};
export default Post;
