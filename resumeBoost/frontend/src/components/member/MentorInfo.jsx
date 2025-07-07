import React, { useEffect, useState } from 'react';
import { S3URL } from '../../util/constant';
import jwtAxios from '../../util/jwtUtils';
import axios from 'axios';
import { EC2_URL } from '../../constans';

const MentorInfo = ({ myId }) => {
  const [member, setMember] = useState(null);
  const [content, setContent] = useState('');
  const [previewURL, setPreviewURL] = useState(null);
  const [file, setFile] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    const memberFn = async () => {
      try {
        const res = await axios.get(`http://${EC2_URL}:8090/member/memberDetail/${myId}`);
        setMember(res.data.member);
        setContent(res.data.member.detail.replace(/<br\s*\/?>/g, '\n')); // <br />를 \n으로 변환하여 상태에 저장
      } catch (error) {
        console.error(error);
      }
    };
    memberFn();
  }, [myId]); // myId가 변경될 때마다 호출

  const handleContentChange = (e) => {
    const value = e.target.value;
    if (value.length <= 5000) {
      setContent(value);
      setErrorMessage('');
    } else {
      setErrorMessage('내용은 5000자를 넘길 수 없습니다.');
    }
  };

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);

    if (selectedFile) {
      const fileReader = new FileReader();
      fileReader.onloadend = () => {
        setPreviewURL(fileReader.result);
      };
      fileReader.readAsDataURL(selectedFile);
    } else {
      setPreviewURL(null);
    }
  };

  const getImageSource = () => {
    if (previewURL) {
      return previewURL; // 새로 선택한 이미지 미리보기
    } else if (member && member.newPtName) {
      return `${S3URL}${member.newPtName}`; // 서버 이미지
    } else {
      return null; // 이미지 없음
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formattedContent = content.replace(/\n/g, '<br />'); // 엔터를 <br />로 변환

    const formData = new FormData();
    formData.append('id', myId);
    formData.append('detail', formattedContent);

    jwtAxios.put(`http://${EC2_URL}:8090/member/mentor/detail`, formData)
      .then((response) => {
        alert("성공");
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const handleFileSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('id', myId);
    if (file) {
      formData.append('ptFile', file); // 새 이미지가 있을 경우 추가
    } else if (member && member.newPtName) {
      formData.append('newPtName', member.newPtName); // 기존 이미지 유지
    }

    jwtAxios.put(`http://${EC2_URL}:8090/member/mentorPtFile`, formData)
      .then((response) => {
        alert("성공");
      })
      .catch((error) => {
        console.error(error);
      });
  };

  return (
    <div className="mentorInfo">
      <div className="mentorInfo-top">
        <h2>서비스 상세설명</h2>
        <form onSubmit={handleSubmit}>
          <textarea
            id="content"
            value={content}
            onChange={handleContentChange}
            required
            placeholder="내용을 입력해주세요."
            maxLength="5000"
          />
          {errorMessage && <p className="error-message">{errorMessage}</p>}
          <div>
            <button type="submit">등록</button>
          </div>
        </form>
      </div>

      <div className="mentorInfo-bottom">
        <h2>포트폴리오</h2>
        <form onSubmit={handleFileSubmit}>
          <div className="pt">
            <label htmlFor="file">이미지 첨부</label>
            <input type="file" id="file" onChange={handleFileChange} accept="image/*" />
            {(previewURL || (member && member.newPtName)) && (
              <div className="ptView">
                <img src={getImageSource()} alt="미리보기 이미지" />
              </div>
            )}
          </div>
          <div className='butt'>
            <button type="submit">등록</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default MentorInfo;
