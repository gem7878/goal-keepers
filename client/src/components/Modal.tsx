'use client';

import Image, { StaticImageData } from 'next/image';
import React, { SetStateAction, useEffect, useRef, useState } from 'react';
import Image1 from '../../public/assets/images/aurora.jpg';
import {
  handleDeleteGoal,
  handleGetGoalListAll,
  handleUpdateGoal,
} from '@/app/actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender, setStateGoal } from '@/redux/renderSlice';

interface selectDataTypes {
  imageUrl: any;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  shareCnt: number;
  goalId: number;
}

const Modal: React.FC<{
  setOpen: React.Dispatch<SetStateAction<boolean>>;
  selectData: selectDataTypes | null;
  setSelectGoalNum: React.Dispatch<SetStateAction<number | null>>;
}> = ({ setOpen, selectData, setSelectGoalNum }) => {
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const [editTitle, setEditTitle] = useState(selectData?.title);
  const [editDescription, setEditDescription] = useState(
    selectData?.description,
  );
  const [editStartDate, setEditStartDate] = useState(selectData?.startDate);
  const [editEndDate, setEditEndDate] = useState(selectData?.endDate);
  const containerRef = useRef<HTMLElement>(null);

  const reduxGoalData = useSelector(selectRender);
  const dispatch = useDispatch();

  const handleOutsideClick = (e: any) => {
    if (!containerRef.current?.contains(e.target)) {
      setOpen(false);
      setSelectGoalNum(null);
    }
  };
  const handleEditGoal = async () => {
    const updateData = {
      goalId: selectData?.goalId,
      title: editTitle,
      description: editDescription,
      startDate: editStartDate,
      endDate: editEndDate,
      imageUrl: selectData?.imageUrl,
    };
    await handleUpdateGoal(updateData)
      .then((response) => {
        if (response.success) {
          dispatch(setStateGoal(!reduxGoalData.goalBoolean));
          setOpen(false);
          setSelectGoalNum(null);
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };
  const handleRemoveGoal = async () => {
    const deleteData = {
      goalId: selectData?.goalId,
    };
    const confirm = window.confirm('목표를 삭제하시겠습니까?');
    if (confirm) {
      await handleDeleteGoal(deleteData)
        .then((response) => {
          if (response.success === true) {
            dispatch(setStateGoal(!reduxGoalData.goalBoolean));
            setOpen(false);
            setSelectGoalNum(null);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  useEffect(() => {}, []);
  const handleConfirmButton = () => {
    if (isEdit) {
      const confirm = window.confirm('수정을 완료하시겠습니까?');
      if (confirm) {
        setIsEdit(false);
        return handleEditGoal();
      }
    } else {
      setIsEdit(true);
      setOpen(false);
      setSelectGoalNum(null);
    }
  };
  return (
    <div
      className="fixed top-0 w-screen h-screen bg-black bg-opacity-70 flex items-center justify-center"
      onClick={(e) => handleOutsideClick(e)}
    >
      <main className="w-3/4 h-3/5 bg-white opacity-100 " ref={containerRef}>
        <section className="w-full h-1/5 relative">
          <Image
            // src={selectData?.imageUrl}
            src={Image1}
            alt=""
            style={{ width: '100%', height: '100%', objectFit: 'cover' }}
          ></Image>
          <div className="absolute top-0 w-full h-full bg-opacity-50 bg-black flex items-center">
            <h2 className="text-white ml-8 text-base font-bold">
              {isEdit ? (
                <input
                  className="bg-inherit"
                  type="text"
                  value={editTitle}
                  onChange={(e) => setEditTitle(e.target.value)}
                ></input>
              ) : (
                selectData?.title
              )}
            </h2>
          </div>
          {!isEdit && (
            <>
              <button
                className="absolute text-white text-xs bottom-1 right-12"
                onClick={() => setIsEdit(true)}
              >
                edit
              </button>
              <button
                className="absolute text-white text-xs bottom-1 right-2"
                onClick={() => handleRemoveGoal()}
              >
                delete
              </button>
            </>
          )}
        </section>
        <section className="h-3/5 w-full pt-8 px-8 flex flex-col justify-between">
          <div className="w-full h-2/3">
            <textarea
              className="h-full"
              value={editDescription}
              onChange={(e) => setEditDescription(e.target.value)}
              readOnly={isEdit ? false : true}
            ></textarea>
          </div>
          <span className="w-full text-xs">
            {isEdit ? (
              <div className="flex">
                <input
                  placeholder={selectData?.startDate}
                  className="w-1/3"
                  onChange={(e) => setEditStartDate(e.target.value)}
                ></input>
                <span>&nbsp;~&nbsp;</span>
                <input
                  placeholder={selectData?.endDate}
                  className="w-1/3"
                  onChange={(e) => setEditEndDate(e.target.value)}
                ></input>
              </div>
            ) : (
              `${selectData?.startDate} ~ ${selectData?.endDate}`
            )}
          </span>
        </section>
        <section className="h-1/5 w-full flex items-center justify-center">
          <button
            className="w-1/4 bg-orange-300 rounded-lg h-1/3 text-white"
            onClick={() => handleConfirmButton()}
          >
            {isEdit ? '수정' : '확인'}
          </button>
        </section>
      </main>
    </div>
  );
};

export default Modal;
