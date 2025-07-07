import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import jwtAxios from '../../util/jwtUtils';
import { useSelector } from 'react-redux';
import { S3URL } from '../../util/constant';
import { EC2_URL } from '../../constans';

const BoardUpdate = ({ params, boardDetail }) => {
  // console.log('ID:', params.id);
  // console.log('Board Detail:', boardDetail);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isCancelModalOpen, setIsCancelModalOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(boardDetail?.category || '자유게시판');
  const [title, setTitle] = useState(boardDetail?.title || '');
  const formattedContent1 = boardDetail.content.replace(/<br\s*\/?>/g, '\n');
  const [content, setContent] = useState(formattedContent1);
  const [file, setFile] = useState(null);
  const [previewURL, setPreviewURL] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [boardId, setBoardId] = useState(params?.id || '');

  const navigate = useNavigate();
  const isLogin = useSelector((state) => state.loginSlice);

  // boardDetail이 변경되면 폼 데이터 업데이트
  useEffect(() => {
    if (boardDetail) {
      setSelectedCategory(boardDetail.category || '자유게시판');
      setTitle(boardDetail.title || '');
      setContent(formattedContent1);
      setBoardId(boardDetail.id || params?.id);
    }
  }, [boardDetail, params]);

  // boardDetail이 없을 경우 params.id를 통해 데이터 가져오기
  useEffect(() => {
    if (params?.id && !boardDetail) {
      jwtAxios.get(`http://${EC2_URL}:8090/board/detail/${params.id}`)
        .then(response => {
          const fetchedBoardDetail = response.data;
          setSelectedCategory(fetchedBoardDetail.category || '자유게시판');
          setTitle(fetchedBoardDetail.title || '');
          const formattedContent = fetchedBoardDetail.content.replace(/<br\s*\/?>/g, '\n');
          setContent(formattedContent || '');
          setBoardId(fetchedBoardDetail.id);
        })
        .catch(error => {
          console.error("게시글 정보를 가져오는데 실패했습니다.", error);
          alert("게시글 정보를 가져오는데 실패했습니다.");
          navigate("/board");
        });
    }
  }, [params, boardDetail, navigate]);

  const handleOpenModal = () => setIsModalOpen(true);
  const handleOpenCancelModal = () => setIsCancelModalOpen(true);
  const handleCloseCancelModal = () => setIsCancelModalOpen(false);

  const handleCategorySelect = (category) => {
    setSelectedCategory(category);
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
    } else if (boardDetail?.newImgName) {
      return `${S3URL}${boardDetail.newImgName}`; // 서버 이미지
    } else {
      return null; // 이미지 없음
    }
  };

  const handleContentChange = (e) => {
    const value = e.target.value;
    if (value.length <= 2000) {
      setContent(value);
      setErrorMessage('');
    } else {
      setErrorMessage('내용은 2000자를 넘길 수 없습니다.');
    }
  };

  const formattedContent = content.replace(/\n/g, '<br />'); // 엔터를 <br />로 변환

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('id', boardId); // 수정할 게시글 ID 추가
    formData.append('memberId', isLogin.id);
    formData.append('title', title);
    formData.append('content', formattedContent);
    formData.append('category', selectedCategory);
    formData.append('writer', isLogin.NickName);
    formData.append('replyCount', boardDetail.replyCount);
    formData.append('viewCount', boardDetail.viewCount);
    if (file) {
      formData.append('boardImgFile', file); // 새 이미지가 있을 경우 추가
    } else if (boardDetail?.newImgName) {
      formData.append('newImgName', boardDetail.newImgName); // 기존 이미지 유지
    }

    jwtAxios.put(`http://${EC2_URL}:8090/board/update`, formData)
      .then((response) => {
        alert('게시글이 수정되었습니다.');
        navigate(`/board/detail/${boardId}`);
      })
      .catch((error) => {
        alert('게시글 수정에 실패했습니다.');
        console.error(error);
      });
  };

  return (
    <div className="board-write-container">
      {isModalOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>주제를 선택해주세요</h2>
            <div className="category-buttons">
              {['자유게시판', '자기소개서', '이력서', '면접'].map((cat) => (
                <button
                  key={cat}
                  onClick={() => handleCategorySelect(cat)}
                  className={selectedCategory === cat ? 'active' : ''}
                >
                  {cat}
                </button>
              ))}
            </div>
            <div className="modal-actions">
              <button onClick={() => setIsModalOpen(false)}>확인</button>
            </div>
          </div>
        </div>
      )}
      {isCancelModalOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>게시글 작성 화면을 나갈까요?</h2>
            <p>작성 중이던 내용은 삭제되며 복구가 불가능합니다.</p>
            <div className="modal-actions">
              <button onClick={handleCloseCancelModal}>계속 작성하기</button>
              <button onClick={() => window.location.href = '/board'} className="exit-btn">나가기</button>
            </div>
          </div>
        </div>
      )}
      <div className="write-content">
        <h1>게시글 수정</h1>
        <div className="selected-category">
          <p>선택된 주제: {selectedCategory}</p>
          <button onClick={handleOpenModal} className="category-btn">주제 변경</button>
        </div>
        <form onSubmit={handleSubmit} className="write-form">
          <div className="form-group">
            <label htmlFor="title">제목</label>
            <input
              type="text"
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
              placeholder="제목을 입력해주세요."
            />
          </div>
          <div className="form-group">
            <label htmlFor="content">내용</label>
            <textarea
              id="content"
              value={content}
              onChange={handleContentChange}
              required
              placeholder="내용을 입력해주세요."
            />
            {errorMessage && <p className="error-message">{errorMessage}</p>}
          </div>
          <div className="form-group-last">
            <label htmlFor="file">이미지 첨부</label>
            <input type="file" id="file" onChange={handleFileChange} accept="image/*" />
            {(previewURL || boardDetail?.newImgName) && (
              <div className="imgView">
                <img src={getImageSource()} alt="미리보기 이미지" />
              </div>
            )}
          </div>
          <div className="last-button">
            <span className='cancel' onClick={handleOpenCancelModal}>
              취소
            </span>
            <button type="submit" className="submit-btn" disabled={content.length > 2000}>
              수정 완료
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default BoardUpdate;