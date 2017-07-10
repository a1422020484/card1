package com.lbwan.game.porkerComparer;

import java.util.List;

import com.lbwan.game.porkerEnumSet.CardTypeEnum;
import com.lbwan.game.porkerEnumSet.FaceValueEnum;

public class SmallCommonBombComparer extends AbstractComparer{
	public int biggerThanLastMaxPorker(List<Integer> sumbitPorkerList, int nLastMaxHandPorker, int nCurrentMajorCard){
		int nErrorResult = 0;
		// 如果是王炸 则返回
		int nSumbitSize = sumbitPorkerList.size();
		int nFaceValue = this.getFaceValueBySpecficCardType(CardTypeEnum.CARD_TYPE_KING_OF_BOMB_VALUE, sumbitPorkerList, nCurrentMajorCard);
		if(0 != nFaceValue){
			int nSuccessResult = HandPatternCalculator.makeNewHandPattern(CardTypeEnum.CARD_TYPE_KING_OF_BOMB_VALUE, nSumbitSize, nFaceValue);
			return nSuccessResult;
		}
		
		// 是否是大的普通炸
		if(sumbitPorkerList.size() > 5){
			nFaceValue = this.getFaceValueBySpecficCardType(CardTypeEnum.CARD_TYPE_BIG_COMMON_BOMB_VALUE, sumbitPorkerList, nCurrentMajorCard);
			if(0 != nFaceValue){
				int nSuccessResult = HandPatternCalculator.makeNewHandPattern(CardTypeEnum.CARD_TYPE_BIG_COMMON_BOMB_VALUE, nSumbitSize, nFaceValue);
				return nSuccessResult;
			}
		}
		
		// 是否是同花顺
		nFaceValue = this.getFaceValueBySpecficCardType(CardTypeEnum.CARD_TYPE_SAME_COLOR_FLUSH_VALUE, sumbitPorkerList, nCurrentMajorCard);
		if(0 != nFaceValue){
			int nSuccessResult = HandPatternCalculator.makeNewHandPattern(CardTypeEnum.CARD_TYPE_SAME_COLOR_FLUSH_VALUE, nSumbitSize, nFaceValue);
			return nSuccessResult;
		}
		
		// 小的普通炸  比较
		nFaceValue = this.getFaceValueBySpecficCardType(CardTypeEnum.CARD_TYPE_SMALL_COMMON_BOMB_VALUE, sumbitPorkerList, nCurrentMajorCard);
		if(0 == nFaceValue){
			return nErrorResult;
		}
		
		int nCompareResult = this.comparerPiecesNum(nSumbitSize, HandPatternCalculator.getPiecesNumByLastPorker(nLastMaxHandPorker));
		if(FaceValueEnum.Less_Result == nCompareResult){
			return nErrorResult;
		}
		
		if(FaceValueEnum.Greater_Result == nCompareResult){
			int nSuccessResult = HandPatternCalculator.makeNewHandPattern(CardTypeEnum.CARD_TYPE_SMALL_COMMON_BOMB_VALUE, nSumbitSize, nFaceValue);
			return nSuccessResult;
		}
		
		// 张数相同的情况下
		boolean bBiggerResult = this.sumbitPorkerBiggerBySinglePorker(nFaceValue, HandPatternCalculator.getFaceValueByLastPorker(nLastMaxHandPorker), nCurrentMajorCard);
		if(true == bBiggerResult){
			int nSuccessResult = HandPatternCalculator.makeNewHandPattern(CardTypeEnum.CARD_TYPE_SMALL_COMMON_BOMB_VALUE, nSumbitSize, nFaceValue);
			return nSuccessResult;
		}
		
		return nErrorResult; 
	}
}
