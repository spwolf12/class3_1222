package com.itwillbs.spring_mvc_board_pcw.mapper;

import com.itwillbs.spring_mvc_board_pcw.vo.BoardVO;

public interface BoardMapper {

	// 글 쓰기
	int insertBoard(BoardVO board);

}
