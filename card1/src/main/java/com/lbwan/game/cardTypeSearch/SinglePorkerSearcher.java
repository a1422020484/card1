package com.lbwan.game.cardTypeSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lbwan.game.porkerComparer.HandPatternCalculator;
import com.lbwan.game.porkerEnumSet.CardColorEnum;
import com.lbwan.game.porkerEnumSet.CardTypeEnum;
import com.lbwan.game.porkerEnumSet.FaceValueEnum;
import com.lbwan.game.utils.IntToolUtils;

public class SinglePorkerSearcher extends AbstractSearcher{
	public SinglePorkerSearcher(){
		m_nSearcherPieceNum = 1;
	}
	
	public int getSearcherPieceNum(){
		return this.m_nSearcherPieceNum;
	}
	

	public int searchBiggerPorkerThanMax(List<Integer> playerHandPorker, int nMaxHandPattern, int nMajorFaceValue, List<Integer> sumbitPorkerList){
		int nErrorResult = 0;
		boolean bCheckResult = this.checkListIsValid(playerHandPorker);
		if(false == bCheckResult){
			logger.error("SinglePorkerSearcher::searchBiggerPorkerThanMax playerHandPorker isnot Valid Error");
			return nErrorResult;
		}
		
		sumbitPorkerList.clear();
		// 单根查找
		IntToolUtils majorHeartPieceNum = new IntToolUtils();
		int nMajorHeartValue = FaceValueEnum.getSpecficHeartByFaceValue(nMajorFaceValue);
		Map<Integer, SearchHelperHolder> searchHolderMap = this.getSearcherMapFromHandPorker(playerHandPorker, majorHeartPieceNum, nMajorHeartValue);
				
		// 根据面值  和 主牌 获取所有比该值大的牌
		int nFaceValue = HandPatternCalculator.getFaceValueByLastPorker(nMaxHandPattern);
		List<Integer> biggerFaceValue = this.calculateBiggerFaceValue(nFaceValue, nMajorFaceValue, this.getSearcherPieceNum()); 
		if(null == biggerFaceValue){
			logger.error("SinglePorkerSearcher::searchBiggerPorkerThanMax biggerFaceValue null Error");
			return nErrorResult;
		}
		
		
		ParameterUtils parameter = new ParameterUtils();
		parameter.setNeedPorkerNum(this.getSearcherPieceNum());
		parameter.setCardTypeEnumValue(CardTypeEnum.CARD_TYPE_ONE_PIECE_VALUE);
		parameter.setMajorFaceValue(nMajorFaceValue);
		parameter.setMajorHeartValueNum(majorHeartPieceNum.getToolValueByInt());
		parameter.setCurrentFaceValue(nFaceValue);
		

		int nResultHandPattern = this.collectSameFacePorkerByNum(sumbitPorkerList, searchHolderMap, biggerFaceValue, parameter);
		if(0 != nResultHandPattern){
			return nResultHandPattern;
		}
		sumbitPorkerList.clear();
	
	    // 四根炸弹
		List<Integer> smallerFaceValueList = this.calculateSmallerFaceValue(nFaceValue, nMajorFaceValue);
		if(null == smallerFaceValueList){
			logger.error("SinglePorkerSearcher::searchBiggerPorkerThanMax smallerFaceValueList null Error");
			return nErrorResult;
		}
		
		parameter.setNeedPorkerNum(4);
		parameter.setCardTypeEnumValue(CardTypeEnum.CARD_TYPE_SMALL_COMMON_BOMB_VALUE);
		parameter.setMajorFaceValue(nMajorFaceValue);
		parameter.setMajorHeartValueNum(majorHeartPieceNum.getToolValueByInt());
		parameter.setCurrentFaceValue(nFaceValue);
		
		nResultHandPattern = this.collectSameFacePorkerByNum(sumbitPorkerList, searchHolderMap, smallerFaceValueList, parameter);
		if(0 != nResultHandPattern){
			return nResultHandPattern;
		}
		sumbitPorkerList.clear();
		
		// 查找同花顺
		parameter.setCardTypeEnumValue(CardTypeEnum.CARD_TYPE_SAME_COLOR_FLUSH_VALUE);
		parameter.setMajorFaceValue(nMajorFaceValue);
		parameter.setMajorHeartValueNum(majorHeartPieceNum.getToolValueByInt());
		parameter.setFlushContinueDiffNumber(5);
		parameter.setFlushSameFacePorkerNumber(1);
		parameter.setMinValueBeginFace(FaceValueEnum.FACE_VALUE_A_VALUE);
		int nNewHandResult = this.searchSameColorFlush(sumbitPorkerList, searchHolderMap, parameter/*nMajorFaceValue, majorHeartPieceNum.getToolValueByInt()*/);
		if(0 != nNewHandResult){
			return nNewHandResult;
		}
		sumbitPorkerList.clear();
		
		// 查找是否有王炸
		int nKingHandResult = this.isKingBombOfHandPattern(sumbitPorkerList, searchHolderMap);
		if(0 != nKingHandResult){
			return nKingHandResult;
		}
		
		sumbitPorkerList.clear();
		return nErrorResult;
	}	
}



