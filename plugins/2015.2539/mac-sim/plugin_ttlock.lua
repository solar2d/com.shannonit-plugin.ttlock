local Library = require "CoronaLibrary"

local ttlock = Library:new{ name='plugin.ttlock', publisherId='com.ttlock' }

ttlock.setLogLevel = function()
	print( 'setLogLevel invoked.' )
end

ttlock.setAndroidPublisherKey = function()
	print( 'setAndroidPublisherKey invoked.' )
end

ttlock.setIOSPublisherKey = function()
	print( 'setIOSPublisherKey invoked.' )
end

ttlock.setTestModeActive = function()
	print( 'setTestModeActive invoked.' )
end

ttlock.cacheAds = function()
	print( 'cacheAds invoked.' )
end

ttlock.showAd = function()
	print( 'showAd invoked.' )
	native.showAlert( 'Error', 'showAd() only works on real device!', { 'OK' } )
end

ttlock.isReadyToShowAd = function()
	print( 'isReadyToShowAd invoked.' )
	return false
end

ttlock.RewardedVideo_fetch = function()
	print( 'RewardedVideo_fetch invoked.' )
end

ttlock.RewardedVideo_show = function()
	print( 'RewardedVideo_show invoked.' )
	native.showAlert( 'Error', 'RewardedVideo_show() only works on real device!', { 'OK' } )
end

ttlock.RewardedVideo_isReadyToShow = function()
	print( 'RewardedVideo_isReadyToShow invoked.' )
	return false
end

ttlock.RewardedVideo_setUserId = function()
	print( 'RewardedVideo_setUserId invoked.' )
end

return ttlock