package com.loc.newsapp.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.loc.newsapp.presentation.Dimens.MediumPadding2
import com.loc.newsapp.presentation.Dimens.PageIndicatorWidth
import com.loc.newsapp.presentation.common.NewsButton
import com.loc.newsapp.presentation.common.NewsTextButton
import com.loc.newsapp.presentation.onboarding.component.OnBoardingPage
import com.loc.newsapp.presentation.onboarding.component.PageIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    event: (OnBoardingEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pagerState = rememberPagerState(initialPage = 0) {
            pages.size
        }
        val buttonState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    0 -> listOf("", "Next")
                    1 -> listOf("Back", "Next")
                    2 -> listOf("Back", "Get Started")
                    else -> listOf()
                }
            }
        }

        HorizontalPager(state = pagerState) { index ->
            OnBoardingPage(page = pages[index])
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MediumPadding2)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PageIndicator(
                modifier = Modifier.width(PageIndicatorWidth),
                pageSize = pages.size,
                selectedPage = pagerState.currentPage
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val scope = rememberCoroutineScope()
                val textButtonText = buttonState.value[0]
                val buttonText = buttonState.value[1]
                if (textButtonText.isNotEmpty()) {
                    NewsTextButton(text = textButtonText, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                        }
                    })
                }
                NewsButton(text = buttonText, onClick = {
                    scope.launch {
                        if (pagerState.isLastPage()) {
                            event(OnBoardingEvent.SaveAppEntry)
                        } else {
                            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                        }
                    }
                })
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.isLastPage(): Boolean {
    return this.currentPage == (pages.size - 1) // because currentPages index starts with 0
}