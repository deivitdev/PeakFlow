package com.deivitdev.peakflow.presentation.activity_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deivitdev.peakflow.domain.model.WorkoutType
import com.deivitdev.peakflow.presentation.components.SkeletonBox

@Composable
fun ActivityDetailSkeleton(type: WorkoutType?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp)
    ) {
        // Header Skeleton
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonBox(modifier = Modifier.size(56.dp), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                SkeletonBox(modifier = Modifier.width(120.dp).height(24.dp))
                Spacer(modifier = Modifier.height(8.dp))
                SkeletonBox(modifier = Modifier.width(180.dp).height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        // Bento Card Skeleton
        SkeletonBox(
            modifier = Modifier.fillMaxWidth().height(240.dp),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Map Thumbnail (if not strength)
        if (type != WorkoutType.STRENGTH) {
            SkeletonBox(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Chart Skeleton
        SkeletonBox(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            shape = RoundedCornerShape(16.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Zones Skeleton
        SkeletonBox(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = RoundedCornerShape(16.dp)
        )
    }
}
