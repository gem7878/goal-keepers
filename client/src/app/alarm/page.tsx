'use client';
import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretUp, faCaretDown } from '@fortawesome/free-solid-svg-icons';

const Alarm = () => {
  const [focusMenu, setFocusMenu] = useState(0);
  const [focusList, setFocusList] = useState<number | null>(null);

  const menu = ['전체', '알림', '공지', '좋아요', '댓글', '공유'];
  const profileColors = [
    'bg-fuchsia-400',
    'bg-blue-400',
    'bg-amber-400',
    'bg-green-400',
  ];

  const alarmList = [
    {
      type: '알림',
      profile: '광고',
      title: '“밍밍" 님이 “오로라보기" 골을 ',
      date: '2023/11/24',
      content:
        '그렇다면 각 서비스는 알림을 보낼 단말을 어떻게 찾아서 알림을 보낼까? 알림을 보내려면 기기 단말 토큰, 전화번호, 이메일 등의 정보가 필요하다. 이 정보들은 사용자가 앱을 설치하거나, 처음으로 계정을 등록할 때 API서버에서 사용자의 정보를 수집하여 데이터베이스에 저장한다',
    },
    {
      type: '좋아요',
      profile: '좋아요',
      // title: '밍밍님이 좋아요를 눌었습니다!',
      // title: 'ffffffffff',
      title: '좋좋좋좋좋좋좋좋좋좋좋좋좋좋좋좋좋좋좋좋',
      date: '2023/11/24',
      content:
        '그렇다면 각 서비스는 알림을 보낼 단말을 어떻게 찾아서 알림을 보낼까? 알림을 보내려면 기기 단말 토큰, 전화번호, 이메일 등의 정보가 필요하다. 이 정보들은 사용자가 앱을 설치하거나, 처음으로 계정을 등록할 때 API서버에서 사용자의 정보를 수집하여 데이터베이스에 저장한다',
    },
    {
      type: '댓글',
      profile: '댓글',
      title: '밍밍님이 댓글을 달았습니다!',
      date: '2023/11/24',
      content:
        '그렇다면 각 서비스는 알림을 보낼 단말을 어떻게 찾아서 알림을 보낼까? 알림을 보내려면 기기 단말 토큰, 전화번호, 이메일 등의 정보가 필요하다. 이 정보들은 사용자가 앱을 설치하거나, 처음으로 계정을 등록할 때 API서버에서 사용자의 정보를 수집하여 데이터베이스에 저장한다',
    },
    {
      type: '공지',
      profile: '공지',
      title: '105동 아파트 분리수거 위치',
      date: '2023/11/24',
      content:
        '그렇다면 각 서비스는 알림을 보낼 단말을 어떻게 찾아서 알림을 보낼까? 알림을 보내려면 기기 단말 토큰, 전화번호, 이메일 등의 정보가 필요하다. 이 정보들은 사용자가 앱을 설치하거나, 처음으로 계정을 등록할 때 API서버에서 사용자의 정보를 수집하여 데이터베이스에 저장한다',
    },
    {
      type: '공지',
      profile: '공지',
      title: '105동 아파트 분리수거 위치',
      date: '2023/11/24',
      content:
        '그렇다면 각 서비스는 알림을 보낼 단말을 어떻게 찾아서 알림을 보낼까? 알림을 보내려면 기기 단말 토큰, 전화번호, 이메일 등의 정보가 필요하다. 이 정보들은 사용자가 앱을 설치하거나, 처음으로 계정을 등록할 때 API서버에서 사용자의 정보를 수집하여 데이터베이스에 저장한다',
    },
  ];

  return (
    <div className="w-full h-full mt-[20%]">
      <header className="overflow-x-scroll w-full pl-[6%] scrollbar-hide alarm-menu h-[10%]">
        <ul className="flex justify-between p-3 w-[130%]">
          {menu.map((data, index) => {
            return (
              <li
                key={index}
                onClick={() => setFocusMenu(index)}
                className={`${
                  focusMenu === index ? 'bg-orange-300 h-10 font-bold' : ''
                } rounded-full flex justify-center items-center w-16`}
              >
                <p className={`${focusMenu === index ? 'text-white' : ''}`}>
                  {data}
                </p>
              </li>
            );
          })}
        </ul>
      </header>
      <main className="px-[3%] h-[calc(90%-52px)] w-full mt-8 pb-5">
        <section className="h-full overflow-y-scroll w-full px-[6%]">
          <ul className=" flex flex-col gap-4 ">
            {focusMenu === 0 ? (
              alarmList.map((data, index) => {
                return (
                  <li key={index} className="w-full ">
                    <div
                      className="h-20 bg-neutral-100 rounded-xl flex items-center p-[5%] gap-2 justify-between"
                      onClick={() => {
                        focusList === index
                          ? setFocusList(null)
                          : setFocusList(index);
                      }}
                    >
                      <div className="w-[18%]">
                        <div
                          className={`h-[78%] aspect-square	mr-[4%] flex items-center justify-center rounded-full ${
                            profileColors[menu.indexOf(data.type) - 1]
                          }	`}
                        >
                          <h2 className="text-white text-base font-semibold	">
                            {data.profile}
                          </h2>
                        </div>
                      </div>

                      <div className="w-[70%] h-full flex flex-col justify-end">
                        <h3 className="text-base font-extrabold truncate w-full ">
                          {data.title}
                        </h3>
                        <p className="text-xs text-end">{data.date}</p>
                      </div>
                      <div className="w-[10%] flex justify-end">
                        {focusList === index ? (
                          <FontAwesomeIcon icon={faCaretUp} />
                        ) : (
                          <FontAwesomeIcon icon={faCaretDown} />
                        )}
                      </div>
                    </div>
                    {focusList === index ? (
                      <div className="p-2 ">
                        <p className="text-sm">{data.content}</p>
                      </div>
                    ) : (
                      <></>
                    )}
                  </li>
                );
              })
            ) : (
              <>
                {alarmList.map((data, index) => {
                  if (data.type === menu[focusMenu]) {
                    return (
                      <li key={index} className="w-full ">
                        <div
                          className="h-20 bg-neutral-100 rounded-xl flex items-center p-[5%] gap-2 justify-between"
                          onClick={() => {
                            focusList === index
                              ? setFocusList(null)
                              : setFocusList(index);
                          }}
                        >
                          <div className="w-[18%]">
                            <div
                              className={`h-[78%] aspect-square	mr-[4%] flex items-center justify-center rounded-full ${
                                profileColors[menu.indexOf(data.type) - 1]
                              }	`}
                            >
                              <h2 className="text-white text-base font-semibold	">
                                {data.profile}
                              </h2>
                            </div>
                          </div>

                          <div className="w-[70%] h-full flex flex-col justify-end">
                            <h3 className="text-base font-extrabold truncate w-full ">
                              {data.title}
                            </h3>
                            <p className="text-xs text-end">{data.date}</p>
                          </div>
                          <div className="w-[10%] flex justify-end">
                            {focusList === index ? (
                              <FontAwesomeIcon icon={faCaretUp} />
                            ) : (
                              <FontAwesomeIcon icon={faCaretDown} />
                            )}
                          </div>
                        </div>
                        {focusList === index ? (
                          <div className="p-2 ">
                            <p className="text-sm">{data.content}</p>
                          </div>
                        ) : (
                          <></>
                        )}
                      </li>
                    );
                  }
                })}
              </>
            )}
          </ul>
        </section>
      </main>
    </div>
  );
};

export default Alarm;
