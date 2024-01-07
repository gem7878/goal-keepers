'use client';

import { useParams, useRouter } from 'next/navigation';
import React, { useEffect, useState } from 'react';
import { addDays, format } from 'date-fns';
import { DateRange, DayPicker } from 'react-day-picker';
import 'react-day-picker/dist/style.css';
import { handlePostGoalData } from './actions';
import Link from 'next/link';
import { useDispatch, useSelector } from 'react-redux';
import {
  setTitle,
  selectGoalData,
  setStartDate,
  setEndDate,
  setDescription,
} from '@/redux/goalDataSlice';
import { setStateGoal } from '@/redux/renderSlice';

const CreateGoal = ({ params }: { params: { createGoalId: string } }) => {
  const [titleValue, setTitleValue] = useState('');
  const [descriptionValue, setDescriptionValue] = useState('');
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [date, setDate] = useState<{
    startDate: {
      year: number | undefined;
      month: number | undefined;
      day: number | undefined;
    };
    endDate: {
      year: number | undefined;
      month: number | undefined;
      day: number | undefined;
    };
  }>({
    startDate: {
      year: undefined,
      month: undefined,
      day: undefined,
    },
    endDate: {
      year: undefined,
      month: undefined,
      day: undefined,
    },
  });

  const [range, setRange] = useState<DateRange | undefined>();
  const [createGoalId, setCreateGoalId] = useState(1);

  const router = useRouter();

  const reduxGoalData = useSelector(selectGoalData);
  const dispatch = useDispatch();

  useEffect(() => {
    setCreateGoalId(parseInt(params.createGoalId));
  }, [params.createGoalId]);

  useEffect(() => {
    setDate({
      startDate: {
        year: range?.from?.getFullYear(),
        month:
          range?.from?.getMonth() !== undefined
            ? range.from.getMonth() + 1
            : undefined,
        day: range?.from?.getDate(),
      },
      endDate: {
        year: range?.to?.getFullYear(),
        month:
          range?.to?.getMonth() !== undefined
            ? range.to.getMonth() + 1
            : undefined,
        day: range?.to?.getDate(),
      },
    });
  }, [range]);

  const handleSaveGoalData = (e: any) => {
    e.preventDefault();
    if (createGoalId === 1) {
      if (titleValue.length > 0) {
        dispatch(setTitle(titleValue));
        router.push(`/create-goal/${createGoalId + 1}`);
      } else {
        return alert('목표를 입력하세요.');
      }
    } else if (createGoalId === 2) {
      router.push(`/create-goal/${createGoalId + 1}`);
      let formatStartDate =
        date.startDate.year && date.startDate.month && date.startDate.day
          ? `${date.startDate.year}-${String(date.startDate.month).padStart(
              2,
              '0',
            )}-${String(date.startDate.day).padStart(2, '0')}`
          : '';
      let formatEndDate =
        date.endDate.year && date.endDate.month && date.endDate.day
          ? `${date.endDate.year}-${String(date.endDate.month).padStart(
              2,
              '0',
            )}-${String(date.endDate.day).padStart(2, '0')}`
          : '';
      dispatch(setStartDate(formatStartDate));
      dispatch(setEndDate(formatEndDate));
    } else if (createGoalId === 3) {
      router.push(`/create-goal/${createGoalId + 1}`);
      dispatch(setDescription(descriptionValue));
    }
  };

  const handleCreateGoal = async (e: any) => {
    e.preventDefault();

    if (imageFile === null) {
      return alert('이미지를 첨부하세요');
    }
    const goalInformation = {
      title: reduxGoalData.title,
      description: reduxGoalData.description,
      startDate: reduxGoalData.startDate,
      endDate: reduxGoalData.endDate,
    };
    const formData = new FormData();

    const jsonBlob = new Blob([JSON.stringify(goalInformation)], {
      type: 'application/json',
    });
    formData.append('goalInformation', jsonBlob);
    if (imageFile) formData.append('image', imageFile);

    await handlePostGoalData(formData)
      .then((response) => {
        if (response?.ok) {
          alert('목표가 생성되었습니다!');
          dispatch(setStateGoal(true));
          router.push(`/`);
        }
      })
      .catch((error) => console.log(error));
  };

  return (
    <>
      <section className="h-96	w-full flex flex-col	items-center">
        {params.createGoalId === '1' ? (
          <>
            <h1 className="gk-primary-h1">목표의 이름을 설정하세요*</h1>
            <input
              type="text"
              placeholder="이름을 입력하세요."
              className="border-b	w-full outline-none h-10"
              maxLength={24}
              autoFocus
              value={titleValue}
              onChange={(e) => setTitleValue(e.target.value)}
            ></input>
          </>
        ) : params.createGoalId === '4' ? (
          <>
            <h1 className="gk-primary-h1">이미지를 선택하세요*</h1>
            <div className="border w-full h-40">
              <input
                type="file"
                className="w-full"
                onChange={(e) => {
                  setImageFile((e.target.files?.[0] as File) || null);
                }}
              ></input>
            </div>
          </>
        ) : params.createGoalId === '2' ? (
          <>
            <h1 className="gk-primary-h1">기간을 설정하세요(선택)</h1>
            <div className="border w-full h-8 rounded-md flex justify-center align-bottom">
              <div className="flex w-[100px] justify-around ">
                <input
                  type="text"
                  placeholder="2023"
                  className="w-[40%] text-center text-[15px]"
                  value={date.startDate.year || ''}
                  onChange={(e) =>
                    setDate({
                      ...date,
                      startDate: {
                        ...date.startDate,
                        year: Number(e.target.value),
                      },
                    })
                  }
                />
                <span className="text-slate-400	leading-7">-</span>
                <input
                  type="text"
                  placeholder="12"
                  className="w-[20%] text-center text-[15px]"
                  value={date.startDate.month || ''}
                  onChange={(e) =>
                    setDate({
                      ...date,
                      startDate: {
                        ...date.startDate,
                        month: Number(e.target.value),
                      },
                    })
                  }
                />
                <span className="text-slate-400	leading-7">-</span>
                <input
                  type="text"
                  placeholder="23"
                  className="w-[20%] text-center text-[15px]"
                  value={date.startDate.day || ''}
                  onChange={(e) =>
                    setDate({
                      ...date,
                      startDate: {
                        ...date.startDate,
                        day: Number(e.target.value),
                      },
                    })
                  }
                />
              </div>
              <span className="text-slate-400	leading-7 mx-2">~</span>
              <div className="flex w-[100px] justify-around">
                <input
                  type="text"
                  placeholder="2023"
                  className="w-[40%] text-center text-[15px]"
                  value={date.endDate.year || ''}
                  onChange={(e) =>
                    setDate({
                      ...date,
                      endDate: {
                        ...date.endDate,
                        year: Number(e.target.value),
                      },
                    })
                  }
                />
                <span className="text-slate-400	leading-7">-</span>
                <input
                  type="text"
                  placeholder="12"
                  className="w-[20%] text-center text-[15px]"
                  value={date.endDate.month || ''}
                  onChange={(e) =>
                    setDate({
                      ...date,
                      endDate: {
                        ...date.endDate,
                        month: Number(e.target.value),
                      },
                    })
                  }
                />
                <span className="text-slate-400	leading-7">-</span>
                <input
                  type="text"
                  placeholder="23"
                  className="w-[20%] text-center text-[15px]"
                  value={date.endDate.day || ''}
                  onChange={(e) =>
                    setDate({
                      ...date,
                      endDate: {
                        ...date.endDate,
                        day: Number(e.target.value),
                      },
                    })
                  }
                />
              </div>
            </div>
            <DayPicker mode="range" selected={range} onSelect={setRange} />
          </>
        ) : params.createGoalId === '3' ? (
          <>
            <h1 className="gk-primary-h1">상세내용을 입력하세요(선택)</h1>
            <textarea
              className="border resize-none	w-full p-2 h-40 outline-none"
              placeholder="내용을 입력하세요."
              autoFocus
              value={descriptionValue}
              onChange={(e) => setDescriptionValue(e.target.value)}
            ></textarea>
          </>
        ) : (
          <></>
        )}
      </section>
      {createGoalId >= 4 ? (
        <Link className="gk-primary-next-a" href={'/'}>
          <button className="w-full h-full" onClick={handleCreateGoal}>
            완료
          </button>
        </Link>
      ) : (
        <Link
          className="gk-primary-next-a"
          href={`${createGoalId + 1}`}
          onClick={handleSaveGoalData}
        >
          <button className="w-full h-full">다음</button>
        </Link>
      )}
    </>
  );
};

export default CreateGoal;
