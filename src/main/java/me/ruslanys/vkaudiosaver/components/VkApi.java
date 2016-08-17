package me.ruslanys.vkaudiosaver.components;

import me.ruslanys.vkaudiosaver.domain.vk.VkAudioResponse;
import me.ruslanys.vkaudiosaver.exceptions.VkException;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public interface VkApi {

    VkAudioResponse getAudio() throws VkException;

}
