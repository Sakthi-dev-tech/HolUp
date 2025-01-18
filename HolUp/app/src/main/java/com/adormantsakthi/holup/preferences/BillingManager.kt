package com.adormantsakthi.holup.preferences

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.adormantsakthi.holup.R
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchaseHistoryResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages all subscription-related functionality through Google Play Billing.
 * This class handles checking subscription status, purchasing subscriptions,
 * and managing the connection to Google Play Billing services.
 */
class BillingManager(private val context: Context) {
    // BillingClient is the main interface to Google Play Billing
    private var billingClient: BillingClient? = null

    // StateFlow to track and observe subscription status throughout the app
    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed = _isSubscribed.asStateFlow()

    // Define your subscription IDs from Google Play Console
    companion object {
        const val SUBSCRIPTION_MONTHLY = "monthly_subscription"
        const val SUBSCRIPTION_YEARLY = "yearly_subscription"
    }

    init {
        setupBillingClient()
    }

    private fun handlePaymentState(purchase: Purchase) {
        when {
            purchase.purchaseState == Purchase.PurchaseState.PENDING -> {
                // Payment is pending/on hold
                _isSubscribed.value = false
                sendNotification(
                    "Subscription Payment On Hold",
                    "Your subscription is currently on hold. Please update your payment method."
                )
                // You might want to store this state locally
                handlePendingPayment(purchase)
            }
            purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE -> {
                // Payment likely declined or failed
                _isSubscribed.value = false
                sendNotification(
                    "Payment Declined",
                    "Your subscription payment was declined. Please update your payment method."
                )
                // Handle the failed payment
                handleDeclinedPayment(purchase)
            }
            purchase.purchaseState == Purchase.PurchaseState.PURCHASED -> {
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                }
                _isSubscribed.value = true
            }
        }
    }

    private fun handlePendingPayment(purchase: Purchase) {
        // Implement grace period logic if needed
        val gracePeriodDays = 3 // Adjust as needed
        val purchaseTime = purchase.purchaseTime
        val currentTime = System.currentTimeMillis()
        val daysSincePurchase = (currentTime - purchaseTime) / (1000 * 60 * 60 * 24)

        if (daysSincePurchase > gracePeriodDays) {
            // Grace period expired, disable subscription features
            _isSubscribed.value = false
            sendNotification(
                "Subscription Paused",
                "Your subscription has been paused due to payment issues. Please update your payment method within 3 days."
            )
        }
    }

    private fun handleDeclinedPayment(purchase: Purchase) {
        // Implement retry logic if needed
        // You might want to track failed attempts and handle accordingly
        _isSubscribed.value = false

        // Direct user to update payment method
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://play.google.com/store/account/subscriptions")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun endBillingClientConnection() {
        billingClient?.endConnection()
        billingClient = null // Nullify reference to avoid accidental usage
    }

    /**
     * Get Product Details according to the User's location
     */

    fun getProductDetails(productId: String, onResult: (ProductDetails?) -> Unit) {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        billingClient?.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val productDetails = productDetailsList.firstOrNull()

                val basePlanPricingPhase = productDetails
                    ?.subscriptionOfferDetails
                    ?.flatMap { it.pricingPhases.pricingPhaseList } // Flatten all pricing phases
                    ?.firstOrNull { phase ->
                        // Find the phase that is not a free trial and represents the base price
                        phase.billingPeriod != "P0D" && phase.recurrenceMode == 1
                    }

                if (basePlanPricingPhase != null) {
                    // Log or pass the base price and currency to the callback
                    Log.d("BillingManager", "Base Price: ${basePlanPricingPhase.formattedPrice}")
                    onResult(productDetails)
                } else {
                    Log.e("BillingManager", "Base plan not found")
                    onResult(null)
                }
            } else {
                Log.e("BillingManager", "Failed to fetch product details: ${billingResult.debugMessage}")
                onResult(null)
            }
        }
    }

    /**
    Acknowledges purchases after purchase status changes
     */

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient?.acknowledgePurchase(
            acknowledgePurchaseParams,
            object : AcknowledgePurchaseResponseListener {
                override fun onAcknowledgePurchaseResponse(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d("BillingManager", "Purchase acknowledged successfully")
                        sendNotification("Thank you for subscribing!", "We hope you enjoy the Plus features!")
                    } else {
                        Log.e("BillingManager", "Failed to acknowledge the purchase: ${billingResult.debugMessage}")
                        sendNotification("Oops! Something went wrong!", "Please check with the developer!")
                    }
                }
            }
        )
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android 8.0+
        val channel = NotificationChannel(
            "subscription_channel",
            "Subscription Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "subscription_channel")
            .setSmallIcon(R.drawable.palm_logo) // Replace with your notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        Log.d("Notification Built", notification.toString())

        notificationManager.notify(2, notification)
    }


    /**
     * Creates and initializes the connection to Google Play Billing.
     * This is called when the BillingManager is first created and
     * whenever the connection to Google Play Billing is lost.
     */
    fun setupBillingClient() {
        // Initialize the BillingClient with the listener for purchase updates
        val listener = PurchasesUpdatedListener { billingResult, purchases ->
            // Handle purchase updates here
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Process successful purchases
                purchases?.forEach {
                        purchase ->
                    // Handle each purchase
                    var retry = 3
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        // Acknowledge the purchase
                        while (retry > 0 && billingClient?.isReady != true){
                            connectToGooglePlay()
                            retry -= 1
                            delay(1000L)
                        }

                        if (!billingClient?.isReady!!){
                            Log.e("BillingManager", "Billing client not ready")
                            return@forEach
                        } else {
                            Log.d("Billing Manager", "Billing Client is Ready!")
                            handlePaymentState(purchase)
                        }
                    }

                    else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                        // User canceled the purchase flow
                        _isSubscribed.value = false
                    }
                    else {
                        // Handle other error cases
                        _isSubscribed.value = false
                    }
                }
            }
        }

        billingClient = BillingClient.newBuilder(context)
            .setListener(listener)  // Provide the listener
            .enablePendingPurchases() // Required for supporting pending purchases
            .build()

        // Connect to BillingClient
        connectToGooglePlay()
    }

    /**
     * Establishes connection to Google Play Billing service.
     * When connected, it automatically checks the current subscription status.
     */
    private fun connectToGooglePlay() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        // Connection successful, query current subscription status
                        querySubscriptionStatus()
                    }
                    else -> {
                        // Connection failed, reset subscription status
                        _isSubscribed.value = false
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                var retry = 3
                // Retry connection when disconnected 3 times
                while (retry > 0){
                    connectToGooglePlay()
                    retry -= 1
                }
            }
        })
    }


    /**
     * Queries current subscription status from Google Play.
     * Updates the isSubscribed state based on the result.
     */
    private fun querySubscriptionStatus() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient?.queryPurchasesAsync(params) { billingResult, purchaseList ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    // Check if any active subscriptions exist
                    _isSubscribed.value = purchaseList.any { purchase ->
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    }
                }
                else -> {
                    _isSubscribed.value = false
                }
            }
        }
    }

    /**
     * Initiates the purchase flow for a subscription.
     * @param activity The activity to launch the purchase flow from
     * @param isMonthly True for monthly subscription, false for yearly
     */
    fun purchaseSubscription(activity: Activity, isMonthly: Boolean) {
        Log.d("BillingManager", "Purchase attempt started")

        if (billingClient?.isReady != true) {
            Log.e("BillingManager", "Billing client not ready")
            setupBillingClient()
            return
        }

        val productId = if (isMonthly) SUBSCRIPTION_MONTHLY else SUBSCRIPTION_YEARLY

        // First, query the product details from Google Play
        val queryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        billingClient?.queryProductDetailsAsync(queryParams) { billingResult, productDetailsList ->
            Log.d("BillingManager", "queryProductDetailsAsync callback triggered")
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d("BillingManager", "Product details fetched successfully: $productDetailsList")
                val productDetails = productDetailsList.firstOrNull() ?: return@queryProductDetailsAsync
                val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
                    ?: return@queryProductDetailsAsync

                // Launch the purchase flow
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(offerToken)
                                .build()
                        )
                    )
                    .build()

                val billingFlowStatus = billingClient?.launchBillingFlow(activity, billingFlowParams)

                if (billingFlowStatus?.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("BillingManager", "Purchase flow launched successfully")
                } else {
                    Log.e("BillingManager", "Failed to launch purchase flow")
                }
            } else {
                Log.e("BillingManager", "Error in queryProductDetailsAsync: ${billingResult.debugMessage}")
            }
        }
    }

    /**
     * Opens the Google Play subscription management page where users can cancel their subscription.
     */
    fun cancelSubscription(activity: Activity) {
        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
            "https://play.google.com/store/account/subscriptions"
        )))
    }

    /**
     * Query Previous Purchases made by the user for the application
     */

    fun getPurchaseHistory(): MutableState<List<Pair<String, Long>>> {
        val res = mutableStateOf<List<Pair<String, Long>>>(emptyList())

        if (billingClient?.isReady == true) {
            val params = QueryPurchaseHistoryParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()

            billingClient!!.queryPurchaseHistoryAsync(
                params,
                object : PurchaseHistoryResponseListener {
                    override fun onPurchaseHistoryResponse(
                        billingResult: BillingResult,
                        purchaseHistoryList: List<PurchaseHistoryRecord>?
                    ) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            val purchaseList = purchaseHistoryList?.map { purchaseHistoryRecord ->
                                val productID = purchaseHistoryRecord.products.firstOrNull().toString()
                                val purchaseTime = purchaseHistoryRecord.purchaseTime
                                Pair(productID, purchaseTime)
                            } ?: emptyList()

                            // Update the state with the fetched purchase history
                            res.value = purchaseList
                        } else {
                            Log.e("BillingManager", "Error fetching purchase history: ${billingResult.debugMessage}")
                        }
                    }
                }
            )
        } else {
            Log.e("PurchaseHistory", "BillingClient is not ready.")
        }

        return res
    }
}