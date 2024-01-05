'use client';

import { CreateButton, Modal, MyGoals } from '@/components/index.js';
import { useEffect, useState } from 'react';
import Image1 from '../../public/assets/images/aurora.jpg';
import Image2 from '../../public/assets/images/gem.png';
import { createPortal } from 'react-dom';
import { StaticImageData } from 'next/image';
import { handleConfirmToken, handleGetGoalListAll } from './actions';
import { useDispatch, useSelector } from 'react-redux';
import { selectRender } from '@/redux/renderSlice';

export default function Home() {
  const [isMyGoals, setIsMyGoals] = useState(true);
  const [isOpen, setOpen] = useState(false);
  const [portalElement, setPortalElement] = useState<Element | null>(null);
  const [selectGoalNum, setSelectGoalNum] = useState<number | null>(null);
  const [selectData, setSelectData] = useState<{
    imageUrl: any;
    title: string;
    description: string;
    startDate: string;
    endDate: string;
    shareCnt: number;
    goalId: number;
  } | null>(null);
  const [myGoalList, setMyGoalList] = useState([]);

  const handleFetchGoalListAll = async () => {
    try {
      const response = await handleGetGoalListAll();

      if (response.data) {
        setMyGoalList(response.data.content);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const reduxGoalData = useSelector(selectRender);

  useEffect(() => {
    handleFetchGoalListAll();
  }, [reduxGoalData.goalBoolean]);

  useEffect(() => {
    selectGoalNum !== null ? setOpen(true) : setOpen(false);
    if (selectGoalNum !== null) {
      setOpen(true);
      setSelectData(myGoalList[selectGoalNum]);
    } else {
      setOpen(false);
      setSelectData(null);
    }
  }, [selectGoalNum]);

  useEffect(() => {
    setPortalElement(document.getElementById('portal'));
  }, [isOpen]);

  const handleTab = (boolean: boolean) => {
    setIsMyGoals(boolean);
  };
  return (
    <div className="flex flex-col	w-full h-full items-center justify-center">
      <header className="fixed top-3 right-5">
        <button>alarm</button>
      </header>
      <section className="w-11/12 h-[calc(100%-60px)]  text-white">
        <nav className="w-full h-10">
          <ul className="flex h-full">
            <li
              className={`w-24 ${
                isMyGoals ? `bg-orange-300` : `bg-amber-500`
              } rounded-se-full`}
            >
              <button
                className="w-full h-full pr-3"
                onClick={() => handleTab(true)}
              >
                나의 목표
              </button>
            </li>
            <li
              className={`w-24 ${
                !isMyGoals ? `bg-orange-300` : `bg-amber-500`
              } rounded-se-full`}
            >
              <button
                className="w-full h-full pr-3"
                onClick={() => handleTab(false)}
              >
                팀 목표
              </button>
            </li>
          </ul>
        </nav>
        <MyGoals
          myGoalList={myGoalList}
          setSelectGoalNum={setSelectGoalNum}
        ></MyGoals>
      </section>
      <CreateButton></CreateButton>
      {isOpen && portalElement
        ? createPortal(
            <Modal
              setOpen={setOpen}
              selectData={selectData}
              setSelectGoalNum={setSelectGoalNum}
            />,
            portalElement,
          )
        : null}
    </div>
  );
}
