'use client';

import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import {
  handleCreateComment,
  handleDeleteComment,
  handleGetComment,
  handleUpdateComment,
} from '@/app/post/comment/actions';
import { useDispatch, useSelector } from 'react-redux';
import {
  selectRender,
  setStateAlarmTarget,
  setStateComment,
} from '@/redux/renderSlice';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faEdit,
  faTrash,
  faWindowClose,
} from '@fortawesome/free-solid-svg-icons';
import { selectAlarmData } from '@/redux/alarmDataSlice';

interface CommentBoxTypes {
  postId: number;
  myNickname: string;
}
interface commentTypes {
  commentId: number;
  content: string;
  isMyComment: boolean;
  nickname: string;
  updatedAt: string;
}
const CommentBox: React.FC<CommentBoxTypes> = ({ postId, myNickname }) => {
  const [commentList, setCommentList] = useState<commentTypes[]>([]);
  const [inputContent, setInputContent] = useState('');
  const [commentEditNum, setCommentEditNum] = useState<number | null>(null);
  const [pageable, setPageable] = useState({
    pageNumber: 1,
    last: false,
  });
  const [more, setMore] = useState<boolean>(false);
  const [focusComment, setFocusComment] = useState<number | null>(null);

  const containerRef = useRef<any>(null);

  const reduxCommentData = useSelector(selectRender);
  const reduxAlarmData = useSelector(selectAlarmData);
  const dispatch = useDispatch();

  useEffect(() => {
    onGetComment(pageable.pageNumber, false);
  }, [reduxCommentData.commentBoolean]);

  useEffect(() => {
    if (reduxCommentData.alarmBoolean) {
      onGetTargetComment(reduxAlarmData.commentPage, reduxAlarmData.commentId);
      dispatch(setStateAlarmTarget(false));
    }
  }, [reduxCommentData.alarmBoolean]);

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
      const elements =
        containerRef.current.querySelectorAll('.comment-element');

      if (elements.length > 0) {
        const lastElement = elements[elements.length - 1];

        const lastComment = lastElement.getBoundingClientRect().bottom;
        const parentComment =
          lastElement.parentElement.getBoundingClientRect().bottom;

        if (lastComment - parentComment < 2) {
          setMore(true);
        }
      }
    }
  }, []);

  const onGetComment = async (pageNumber: number, create: boolean) => {
    const formData = {
      postId: postId,
      page: pageNumber,
    };

    const response = await handleGetComment(formData);

    if (response.success) {
      if (create) {
        setCommentList((prevPostData) => [
          ...prevPostData,
          response.data.content[response.data.content.length - 1],
        ]);
      } else {
        if (more) {
          setCommentList((prevPostData) => [
            ...prevPostData,
            ...response.data.content,
          ]);
        } else {
          setCommentList(response.data.content);
        }
        setMore(false);
      }
      setPageable({
        pageNumber: response.data.pageable.pageNumber + 1,
        last: response.data.last,
      });
    }
  };

  const onGetTargetComment = async (
    pageNumber: number,
    targetCommentId: number,
  ) => {
    const formData = {
      postId: postId,
      page: pageNumber,
    };

    const response = await handleGetComment(formData);

    if (response.success) {
      const responseData = response.data.content;

      const index = responseData.findIndex(
        (item: { commentId: number }) => item.commentId === targetCommentId,
      );

      setCommentList(response.data.content);
      setPageable({
        pageNumber: response.data.pageable.pageNumber + 1,
        last: response.data.last,
      });

      containerRef.current.scrollTo({ top: 36 * index });
      setFocusComment(index);
    }
  };

  const onCreateComment = async () => {
    const formData = {
      postId: postId,
      content: inputContent,
    };

    await handleCreateComment(formData)
      .then((response) => {
        if (response.success) {
          setInputContent('');
          onGetComment(pageable.pageNumber, true);
        }
      })
      .catch((error) => console.log(error));
  };

  const onUpdateComment = async () => {
    if (typeof commentEditNum === 'number') {
      const formData = {
        commentId: commentEditNum,
        content: inputContent,
      };
      await handleUpdateComment(formData)
        .then((response) => {
          if (response.success) {
            setInputContent('');
            setCommentEditNum(null);
            dispatch(setStateComment(!reduxCommentData.commentBoolean));
          }
        })
        .catch((error) => console.log(error));
    }
  };

  const onDeleteComment = async (index: number) => {
    const formData = {
      commentId: index,
    };
    const confirm = window.confirm('댓글을 삭제하시겠습니까?');
    if (confirm) {
      await handleDeleteComment(formData)
        .then((response) => {
          if (response.success) {
            dispatch(setStateComment(!reduxCommentData.commentBoolean));
          }
        })
        .catch((error) => console.log(error));
    }
  };

  const handleCheckLastPage = () => {
    const pageNumber = pageable.pageNumber + 1;
    if (pageable.last) {
      console.log('마지막 페이지 입니다.');
    } else {
      onGetComment(pageNumber, false);
    }
  };

  return (
    <div className="w-full max-h-[30%] min-h-[50px] flex-col text-sm">
      <div className="w-full h-[calc(100%-9px)] mt-2 border rounded-lg p-2">
        <ul
          className="w-full h-[calc(100%-29px)] overflow-y-scroll mb-1"
          ref={containerRef}
        >
          {commentList.map((list, index) => {
            return (
              <li
                key={index}
                className={`flex-col w-full h-9 p-[2px] comment-element text-gray-700 ${
                  focusComment === index && 'bg-orange-100'
                }`}
              >
                <div className="flex text-xs w-full justify-between">
                  <h4 className=" font-bold">{list.nickname}</h4>
                  {myNickname === list.nickname &&
                    (commentEditNum ? (
                      commentEditNum === list.commentId ? (
                        <div className="flex gap-1 mr-2">
                          <FontAwesomeIcon
                            className="text-gray-600"
                            onClick={() => {
                              setCommentEditNum(null);
                              setInputContent('');
                            }}
                            icon={faWindowClose}
                          />
                        </div>
                      ) : (
                        <></>
                      )
                    ) : (
                      <div className="flex gap-1 mr-2">
                        <FontAwesomeIcon
                          className="text-gray-600"
                          onClick={() => {
                            setCommentEditNum(list.commentId);
                            setInputContent(list.content);
                          }}
                          icon={faEdit}
                        />
                        <FontAwesomeIcon
                          className="text-gray-600"
                          onClick={() => onDeleteComment(list.commentId)}
                          icon={faTrash}
                        />
                      </div>
                    ))}
                </div>
                <p
                  className={`text-xs w-auto ${
                    commentEditNum === list.commentId && 'bg-orange-100'
                  }`}
                >
                  {list.content}
                </p>
              </li>
            );
          })}
        </ul>
        <div className="w-full flex justify-between h-[25px]">
          <input
            type="text"
            className="border rounded-lg w-5/6 pl-2 text-xs text-gray-800"
            placeholder="댓글을 입력하세요."
            value={inputContent}
            onChange={(e) => setInputContent(e.target.value)}
          />
          {commentEditNum !== null ? (
            <button
              className="w-[13%] bg-orange-300 rounded-lg text-xs text-white border border-white"
              onClick={() => onUpdateComment()}
            >
              수정
            </button>
          ) : (
            <button
              className="w-[13%] bg-orange-300 rounded-lg text-xs text-white border border-white"
              onClick={() => onCreateComment()}
            >
              확인
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
export default CommentBox;
