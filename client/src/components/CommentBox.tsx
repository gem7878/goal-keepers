'use client';

import React, { useEffect, useState } from 'react';
import {
  handleCreateComment,
  handleDeleteComment,
  handleGetComment,
  handleUpdateComment,
} from '@/app/community/comment/actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStateComment } from '@/redux/renderSlice';

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
  const [CommentEditNum, setCommentEditNum] = useState<number | null>(null);

  const reduxCommentData = useSelector(selectRender);
  const dispatch = useDispatch();

  useEffect(() => {
    onGetComment();
  }, [reduxCommentData.commentBoolean]);

  const onGetComment = async () => {
    const formData = {
      postId: postId,
      page: 1,
    };
    await handleGetComment(formData)
      .then((response) => {
        if (response.success) {
          setCommentList(response.data.content);
        }
      })
      .catch((error) => console.log(error));
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
          dispatch(setStateComment(!reduxCommentData.commentBoolean));
        }
      })
      .catch((error) => console.log(error));
  };
  const onUpdateComment = async () => {
    if (typeof CommentEditNum === 'number') {
      const formData = {
        commentId: CommentEditNum,
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

  return (
    <div className="w-full h-[35%] flex-col text-sm">
      <h3 className="h-4">댓글</h3>
      <div className="w-full h-[calc(100%-20px)] mt-1 border rounded-lg p-2">
        <ul className="w-full h-3/4 overflow-y-scroll">
          {commentList.map((list, index) => {
            return (
              <li key={index} className="flex-col w-full">
                <div className="flex text-xs w-full justify-between">
                  <h4 className=" font-bold">{list.nickname}</h4>
                  {myNickname === list.nickname &&
                    (CommentEditNum ? (
                      CommentEditNum === list.commentId ? (
                        <div className="flex gap-1 mr-2">
                          <button
                            onClick={() => {
                              setCommentEditNum(null);
                              setInputContent('');
                            }}
                          >
                            cancel
                          </button>
                        </div>
                      ) : (
                        <></>
                      )
                    ) : (
                      <div className="flex gap-1 mr-2">
                        <button
                          onClick={() => {
                            setCommentEditNum(list.commentId);
                            setInputContent(list.content);
                          }}
                        >
                          edit
                        </button>
                        <button onClick={() => onDeleteComment(list.commentId)}>
                          delete
                        </button>
                      </div>
                    ))}
                </div>

                <p
                  className={`text-xs w-auto ${
                    CommentEditNum === list.commentId && 'bg-orange-100'
                  }`}
                >
                  {list.content}
                </p>
              </li>
            );
          })}
        </ul>
        <div className="w-full flex justify-between h-1/4">
          <input
            type="text"
            className="border rounded-lg w-5/6 pl-2 text-xs"
            placeholder="댓글을 입력하세요."
            value={inputContent}
            onChange={(e) => setInputContent(e.target.value)}
          />
          {CommentEditNum !== null ? (
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
