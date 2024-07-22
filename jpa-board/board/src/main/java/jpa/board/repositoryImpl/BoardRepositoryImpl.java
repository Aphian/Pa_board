package jpa.board.repositoryImpl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jpa.board.dto.BoardDto;
import jpa.board.dto.QBoardDto;
//import jpa.board.dto.QBoardFileDto;
//import jpa.board.entity.BoardFile;

import jpa.board.repository.CustomBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
//import java.util.stream.Collectors;

import static jpa.board.entity.QBoard.board;
//import static jpa.board.entity.QBoardFile.boardFile;
import static jpa.board.entity.QMember2.member2;

@Repository
public class BoardRepositoryImpl implements CustomBoardRepository{
	
	private final JPAQueryFactory jpaQueryFactory;

	public BoardRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}
	
	@Override
	public Page<BoardDto> seleteBoardList(String searchVal, Pageable pageable) {
		
		List<BoardDto> content = getBoardMemberDtos(searchVal, pageable);
		Long count = getCount(searchVal);
		return new PageImpl<>(content, pageable, count);
		
	}
	
    private Long getCount(String searchVal){
        Long count = jpaQueryFactory
                .select(board.count())
                .from(board)
                //.leftJoin(board.member, member)   //검색조건 최적화
                .fetchOne();
        return count;
        
    }
    
    private List<BoardDto> getBoardMemberDtos(String searchVal, Pageable pageable){
        List<BoardDto> content = jpaQueryFactory
                .select(new QBoardDto(
                         board.id
                        ,board.title
                        ,board.content
                        ,board.regDate
                        ,board.uptDate
                        ,board.viewCount
                        ,member2.username))
                .from(board)
                .leftJoin(board.member2, member2)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }
	
	private BooleanExpression containsSearch(String searchVal) {
		
		return searchVal != null ? board.title.contains(searchVal) : null;
		
	}


	
}
